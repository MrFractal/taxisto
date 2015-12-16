package ru.trendtech.services.driver.search;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.trendtech.common.mobileexchange.model.common.CustomException;
import ru.trendtech.domain.*;
import ru.trendtech.domain.courier.Order;
import ru.trendtech.repositories.*;
import ru.trendtech.repositories.courier.OrderRepository;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.courier.OrderService;
import ru.trendtech.services.notifications.node.NodeJsNotificationsService;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.services.client.ClientService;
import ru.trendtech.services.notifications.push.App42PushNotificationService;
import ru.trendtech.services.sms.ServiceSMSNotification;
import ru.trendtech.utils.DateTimeUtils;
import ru.trendtech.utils.MoneyUtils;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by petr on 09.04.2015.
 */
@Service
public class FindDriversService implements FindDrivers {
    private static final Logger LOGGER = LoggerFactory.getLogger(FindDriversService.class);
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private AdministrationService administrationService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private LocationRepository driverLocationRepository;
    @Autowired
    private MissionFantomDriverRepository missionFantomDriverRepository;
    @Autowired
    private NodeJsNotificationsService nodeJsNotificationsService;
    @Autowired
    private ServiceSMSNotification serviceSMSNotification;
    @Autowired
    private ClientService clientService;
    @Autowired
    private MissionCanceledRepository missionCanceledRepository;
    @Autowired
    private ReasonRepository reasonRepository;
    @Autowired
    private App42PushNotificationService app42PushNotificationService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CommonService commonService;
    @Autowired
    private OrderService orderService;

    private HashMap queueOrder = new HashMap();
    private HashMap queueMission = new HashMap();


    @Override
    public void findDrivers(Mission mission, int secSearch, float radius) {
        nodeJsNotificationsService.findDrivers(mission, secSearch, radius);
    }

    @Override
    public void findCouriers(Order order, int secSearch) {
        nodeJsNotificationsService.findCouriers(order, secSearch);
    }

    @Override
    public Mission getActualMission(long missionId) {
       return missionRepository.findOne(missionId);
    }

    @Override
    public Order getActualOrder(long orderId) {
        return orderRepository.findOne(orderId);
    }


    @Override
    public void cancelOrder(Order order, int secSearch, long reasonId) {
        //Reason reason = reasonRepository.findOne(reasonId);
        removeThreadSearchFromQueue(0, order.getId());

        orderService.cancelOrderByServer(order);

        nodeJsNotificationsService.courierOrderCanceledToClientAndCourier(order);
        app42PushNotificationService.courierOrderCanceledToClientAndCourier(order);
        serviceSMSNotification.sendCustomSMS(order.getClient().getPhone(), "К сожалению, курьер не найден.", "");
    }

    /*
        order.setState(Order.State.CANCELED);
        order.setReasonCancel("Отменен сервером");
        order.setTimeOfCanceling(DateTimeUtils.nowNovosib_GMT6());
        orderRepository.save(order);
        commonService.saveChangeState(order);
        Client client = order.getClient();
        client.setOrder(null);
        clientRepository.save(client);
     */


    @Override
    public void cancelMission(SearchMode searchMode, Mission mission, int secSearch, long reasonId, String ip) {
       Reason reason = reasonRepository.findOne(reasonId);

       if(searchMode.equals(SearchMode.SEARCH_DRIVER_WHEN_MISSION_WITH_FANTOM)){
           LOGGER.info(String.format("Отменяем миссию с id=%s и останавливаем поток по таймауту", mission.getId()));

           removeThreadSearchFromQueue(mission.getId(), 0);
           clearCurrentMissionForAllFantomDrivers(mission);
           updateFantomTable(mission, secSearch);

           /* сохраняем информацию об отмене с указанием причины отмены */
           administrationService.missionCanceledStore(mission.getId(), "driver", mission.getDriverInfo().getId(), mission.getState().name(), reason, "cancel by fantom");

           /* оповещаем об отмене */
           nodeJsNotificationsService.missionBecameUnavailable(mission.getId(), mission.getDriverInfo().getId());
           nodeJsNotificationsService.missionDriverCanceled(mission, "");

           if(reason!=null && reason.getClientBonus() > 0){
               /* сумму необходимо брать из reason */
               serviceSMSNotification.sendCustomSMS(mission.getClientInfo().getPhone(), String.format("Водитель отменил заказ. В качестве извинений в Ваш кошелек начислено %s руб.", reason.getClientBonus()), "");
               administrationService.operationWithBonusesClient(mission.getClientInfo().getId(), mission.getId(), reason.getClientBonus(), 20, null, "фантом отменил заказ", null);
           }
       }
       else
       if(searchMode.equals(SearchMode.SEARCH_DRIVER_WHEN_MISSION_CANCELED_BY_DRIVER)){
           /* в течении определенного времени никто не взял отмененный заказ. штрафуем водителя отказавшегося от данного заказа */
           LOGGER.info(String.format("Отменяем миссию с id=%s и останавливаем поток по таймауту", mission.getId()));
           removeThreadSearchFromQueue(mission.getId(), 0);
           nodeJsNotificationsService.missionBecameUnavailable(mission.getId(), mission.getDriverInfo().getId());

               MissionCanceled missionCanceled = missionCanceledRepository.findByMissionId(mission.getId());
               /* берем коммент водителя указанный при отмене от заказа (если был), прибавляем к нему текст причины отказа от заказа (по таймауту, не ответил на предложение автосерч...) */
               Reason prevoiusReason = missionCanceled.getReasonInfo();
               missionCanceled.setReason(missionCanceled.getReason()+".\n"+reason.getReason());
               missionCanceledRepository.save(missionCanceled);
                   if(reason.getClientBonus()>0){
                       /* шлем смс сообщение о том, что мы дарим бабло */
                       serviceSMSNotification.sendCustomSMS(mission.getClientInfo().getPhone(), "Водитель отменил заказ. В качестве извинений в Ваш кошелек начислено" + prevoiusReason.getClientBonus() +"руб.", "");

                       /* начисляем клиенту бонусы указанные в причине  */
                       administrationService.operationWithBonusesClient(mission.getClientInfo().getId(), mission.getId(), prevoiusReason.getClientBonus(), 21, null, "водитель отменил заказ", null);

                       /* штрафуем водителя на сумму, указанную в причине */
                       if (mission.getDriverInfo() != null) {
                           administrationService.updateDriverBalanceSystem(mission.getDriverInfo().getId(), mission.getId(), -prevoiusReason.getFine(), 1);
                       }

                       /* оповещаем клиента о том, что водитель отменил заказ [push, node] */
                       nodeJsNotificationsService.missionDriverCanceled(mission, "");

                       String message = commonService.getPropertyValue("driver_canceled_mission_push_mess");
                       app42PushNotificationService.missionDriverCanceled(mission, message);
                   }
       }
       else
       if(searchMode.equals(SearchMode.SEARCH_DRIVER_WHEN_MISSION_MULTIPLE_SUPPORT)){
           removeThreadSearchFromQueue(mission.getId(), 0);
           clientService.cancelMissionByClient(mission.getId(), mission.getClientInfo().getId(), reason, ip); // true - отмена фантомным водителем, true - клиентом
           // чистим multiple_id на клиенте если у него НЕТ активных миссий
           cleaneIfActiveMissionEmpty(mission);
       }
       else
       if(searchMode.equals(SearchMode.SEARCH_COURIER)){
           removeThreadSearchFromQueue(mission.getId(), 0);
           clientService.cancelMissionByClient(mission.getId(), mission.getClientInfo().getId(), reason, ip); // true - отмена фантомным водителем, true - клиентом
           // чистим multiple_id на клиенте если у него НЕТ активных миссий
           cleaneIfActiveMissionEmpty(mission);
       }
    }



    @Override
    public void driverChange(Mission mission) {
       nodeJsNotificationsService.driverChange(mission, "");
       String message = commonService.getPropertyValue("driver_change_push_mess");
       app42PushNotificationService.driverChange(mission, message);
    }



    // начать поиск водителей
    @Override
    public void findMultipleDrivers(MultipleMission multipleMission) throws ExecutionException, InterruptedException, JSONException {
         for(Mission mission: multipleMission.getMultipleMissions()){
             if(isRun(mission, null)){
                 throw new CustomException(2, String.format("Поиск водителя для миссии id=%s уже запущен", mission.getId()));
             }
                 StartSearch startSearchWhenMissionMultipleSupport = new StartSearch();
                 startSearchWhenMissionMultipleSupport.setSearchMode(SearchMode.SEARCH_DRIVER_WHEN_MISSION_MULTIPLE_SUPPORT);
                 queueMission.put(mission.getId(), createThreadSearch(mission, null, startSearchWhenMissionMultipleSupport)); // .getScheduledFuture()
         }
    }



    @Override
    public void findDriversWhenMissionWithFantomDriver(Mission mission) throws ExecutionException, InterruptedException, JSONException {
         if(isRun(mission, null)){
             throw new CustomException(2, String.format("Поиск водителя для миссии id=%s уже запущен", mission.getId()));
         }
             StartSearch startSearchWhenMissionWithFantom = new StartSearch();
             startSearchWhenMissionWithFantom.setSearchMode(SearchMode.SEARCH_DRIVER_WHEN_MISSION_WITH_FANTOM);
             queueMission.put(mission.getId(), createThreadSearch(mission, null, startSearchWhenMissionWithFantom)); // .getScheduledFuture()
    }




    @Override
    public void findCourier(Order order){
        if (isRun(null, order)) {
            throw new CustomException(2, String.format("Поиск курьера для заказа id=%s уже запущен", order.getId()));
        }
        order = orderRepository.findOne(order.getId());

        StartSearch startSearchCourier = new StartSearch();
        startSearchCourier.setSearchMode(SearchMode.SEARCH_COURIER);

        startSearchCourier = createThreadSearch(null, order, startSearchCourier);
        queueOrder.put(order.getId(), startSearchCourier);
    }




    @Override
    public void findDriversWhenDriverCanceledMission(Mission mission) throws ExecutionException, InterruptedException, JSONException {
        if(isRun(mission, null)){
          throw new CustomException(2, String.format("Поиск водителя для миссии id=%s уже запущен", mission.getId()));
        }
          StartSearch startSearchWhenMissionCancelByDriver = new StartSearch();
          startSearchWhenMissionCancelByDriver.setSearchMode(SearchMode.SEARCH_DRIVER_WHEN_MISSION_CANCELED_BY_DRIVER);
          queueMission.put(mission.getId(), createThreadSearch(mission, null, startSearchWhenMissionCancelByDriver)); // .getScheduledFuture()
    }



    @Override
    public StartSearch createThreadSearch(Mission mission, Order order, StartSearch startSearch) {
        startSearch.setMission(mission);
        startSearch.setOrder(order);
        startSearch.setCounter(0);
        startSearch.setSearchSecond(0);
        startSearch.setFindDriversService(this);
        ScheduledFuture<StartSearch> scheduledFuture = (ScheduledFuture<StartSearch>) taskScheduler.scheduleWithFixedDelay(startSearch, 1000);
        startSearch.setScheduledFuture(scheduledFuture);
        return startSearch;
    }






    @Override
    public void clearCurrentMissionForAllFantomDrivers(Mission mission) {
        List<Driver> drivers = driverRepository.findByTypeXAndCurrentMission(Boolean.TRUE, mission);
        if(!CollectionUtils.isEmpty(drivers)){
            for(Driver driver: drivers){
                driver.setCurrentMission(null);
            }
            driverRepository.save(drivers);
        }
        List<DriverLocation> locations = driverLocationRepository.findByMissionAndDriverTypeX(mission, Boolean.TRUE);
        if(!CollectionUtils.isEmpty(locations)){
            for(DriverLocation drvLoc: locations){
                drvLoc.setMission(null);
            }
            driverLocationRepository.save(locations);
        }
    }





    @Override
    public void updateFantomTable(Mission mission, int secSearch) {
      MissionFantomDriver missionFantomDriver = missionFantomDriverRepository.findByMission(mission);
      if(missionFantomDriver!=null){
          if(mission.getDriverInfo()!=null && !mission.getDriverInfo().isTypeX()){
              missionFantomDriver.setDiver(mission.getDriverInfo());
          }
          missionFantomDriver.setGeneralTimeSecSearch(secSearch);
          missionFantomDriverRepository.save(missionFantomDriver);
      }
    }






    // остановить поиск водителя для missionId
    public void stopThreadFindDrivers(long missionId) throws ExecutionException, InterruptedException {
        if(CollectionUtils.isEmpty(queueMission)){
            throw new CustomException(2, "Очередь потоков пуста");
        }
        if(!queueMission.containsKey(missionId)){
            throw new CustomException(4, String.format("По миссии id %s не найдено активных потоков", missionId));
        }
            removeThreadSearchFromQueue(missionId, 0);
    }


    @Override
    public void sumIncrease(Mission mission, int sum, int sumIncrOrigin, int secSearch) {
      mission.getStatistics().setSumIncrease(MoneyUtils.getRubles(sum));
      missionRepository.save(mission);
      MissionFantomDriver missionFantomDriver = missionFantomDriverRepository.findByMission(mission);
          if(missionFantomDriver!=null){
              missionFantomDriver.setSumIncrease(missionFantomDriver.getSumIncrease() + sumIncrOrigin);
              missionFantomDriver.setGeneralTimeSecSearch(secSearch);
              missionFantomDriverRepository.save(missionFantomDriver);
          }
    }


    // печать содержимого очереди
    @Override
    public void printQueueMission() throws ExecutionException, InterruptedException {
        Collection<StartSearch> collection = queueMission.values();
        if(!CollectionUtils.isEmpty(collection)){
            for(StartSearch startSearch: collection){
                LOGGER.info("Активная задача: "+ startSearch.getScheduledFuture());
            }
        }else{
                LOGGER.info("Очередь пуста");
        }
    }




    @Override
    public boolean isRun(Mission mission, Order order) {
     boolean result = false;
        if(mission != null){
            if(queueMission.containsKey(mission.getId())){
                result = true;
            }
        }
        if(order != null){
            if(queueOrder.containsKey(order.getId())){
                result = true;
            }
        }
        return result;
    }






    @Override
    public void cleaneIfActiveMissionEmpty(Mission mission){
       boolean hasActiveMission = false;
       if(mission!=null){
           MultipleMission multipleMission = mission.getMultipleMission();
           if(multipleMission!=null){
               Set<Mission> missions = multipleMission.getMultipleMissions();
               if(!CollectionUtils.isEmpty(missions)){
                    for(Mission miss: missions){
                        if(!EnumSet.of(Mission.State.COMPLETED, Mission.State.CANCELED).contains(miss.getState())){
                            // миссия активна
                            hasActiveMission = true;
                             break;
                        }
                    }
                    if(hasActiveMission){
                        // чистим на клиенте
                        Client client = mission.getClientInfo();
                          if(client!=null){
                              client.setMultipleMission(null);
                              clientRepository.save(client);
                          }
                    }
               }
           }
       }
    }





    @Override
    public void removeThreadSearchFromQueue(long missionId, long orderId){
       if(missionId != 0){
           if(queueMission.containsKey(missionId)){
               StartSearch startSearch = (StartSearch)queueMission.get(missionId);
               ScheduledFuture<StartSearch> scheduledFuture = startSearch.getScheduledFuture();
               if(scheduledFuture!=null){
                   scheduledFuture.cancel(true);
               }
                   queueMission.remove(missionId);
           }
       }
       if(orderId != 0){
           if(queueOrder.containsKey(orderId)){
               StartSearch startSearch = (StartSearch)queueOrder.get(orderId);
               ScheduledFuture<StartSearch> scheduledFuture = startSearch.getScheduledFuture();
               if(scheduledFuture!=null){
                   scheduledFuture.cancel(true);
               }
                   queueOrder.remove(orderId);
           }
       }
    }


    @Override
    public void askClientForAutoSearch(Mission mission) {
      nodeJsNotificationsService.askClientForAutoSearch(mission);
    }



    public static enum SearchMode {
     SEARCH_DRIVER_WHEN_MISSION_WITH_FANTOM,
     SEARCH_COURIER,
     SEARCH_DRIVER_WHEN_MISSION_MULTIPLE_SUPPORT,
     SEARCH_DRIVER_WHEN_MISSION_CANCELED_BY_DRIVER,;
    }

    public HashMap getQueueMission() {
      return queueMission;
    }

    public void setQueueMission(HashMap queueMission) {
      this.queueMission = queueMission;
    }

    public HashMap getQueueOrder() {
        return queueOrder;
    }

    public void setQueueOrder(HashMap queueOrder) {
        this.queueOrder = queueOrder;
    }
}
