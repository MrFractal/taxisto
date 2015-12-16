package ru.trendtech.services.driver.search;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.DriverLocation;
import ru.trendtech.domain.Mission;
import ru.trendtech.domain.MissionFantomDriver;
import ru.trendtech.repositories.DriverRepository;
import ru.trendtech.repositories.LocationRepository;
import ru.trendtech.repositories.MissionFantomDriverRepository;
import ru.trendtech.repositories.MissionRepository;
import ru.trendtech.services.notifications.node.NodeJsNotificationsService;
import ru.trendtech.services.notifications.node.NodeJsService;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.services.client.ClientService;
import ru.trendtech.services.sms.ServiceSMSNotification;
import ru.trendtech.utils.GeoUtils;
import ru.trendtech.utils.MoneyUtils;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by petr on 24.04.2015.
 */

public class StartSearchDrivers implements Runnable{
    private static final Logger LOGGER = LoggerFactory.getLogger(StartSearchDrivers.class);
    private int searchSecond = 0; // секунда поиска
    public static boolean isAskedClient = false;
    private FindDriversService findDriversService;
    private JSONObject jsonMessageForDriver;
    private String description;
    private int counter = 0;
    private float radius;
    private Mission mission;
    private String name;
    private String printInfo = "none";
    private boolean fantomSupport = false;
    private boolean steps[] = {false, false, false, false};
    private ScheduledFuture<StartSearchDrivers> scheduledFuture;
    private AdministrationService administrationService;
    private DriverRepository driverRepository;
    private LocationRepository driverLocationRepository;
    private MissionFantomDriverRepository missionFantomDriverRepository;
    private ClientService clientService;
    private ServiceSMSNotification serviceSMSNotification;
    private NodeJsNotificationsService nodeJsNotificationsService;
    private MissionRepository missionRepository;


    private void defaultSearch(Mission.State state){

        if(!EnumSet.of(Mission.State.NEW, Mission.State.AUTO_SEARCH).contains(mission.getState())) {
            // изменился статус миссии, останавливаем поток
            LOGGER.info(String.format("Статус миссии id= %s изменился. Останавливам поток", getMission().getId()));
            setPrintInfo(String.format("Статус миссии id= %s изменился. Останавливам поток", getMission().getId()));

            findDriversService.removeThreadSearchFromQueue(getMission().getId(), 0);
            // чистим multiple_id на клиенте если у него НЕТ активных миссий
            findDriversService.cleaneIfActiveMissionEmpty(getMission());
        }

        if(counter > 0 && counter <= 60) {
            // обычный режим
            LOGGER.info("Ищу водилу в обычном режиме для: "+getMission().getId()+" "+counter+" секунд");
            setPrintInfo("Ищу водилу в обычном режиме для: " + getMission().getId() + " " + counter + " секунд");
        }
        else if(counter > 60 && counter <= 75) {
            // отсылаем клиенту сообщение: Вы хотите продолжить поиск в режиме автосерча [ждем ответа 10 секунд]
            LOGGER.info("Отправлено ли событие на нод с предложением перейти в AUTO_SEARCH: "+ (isAskedClient() ? "ДА":"НЕТ"));
            setPrintInfo("Отправлено ли событие на нод с предложением перейти в AUTO_SEARCH: " + (isAskedClient() ? "ДА" : "НЕТ"));
            if(isAskedClient()){
                // уже спроил у клиента по поводу перехода в режим автопоиска
                LOGGER.info("Ждем ответа пользователя для включения рнжима AUTO_SEARCH для: "+getMission().getId());
                setPrintInfo("Ждем ответа пользователя для включения рнжима AUTO_SEARCH для: "+getMission().getId());
            }else{

                    LOGGER.info("Вы хотите перейти в режим автопоиска для миссии: "+getMission().getId());
                    setPrintInfo("Вы хотите перейти в режим автопоиска для миссии: "+getMission().getId());
                    askClientForAutoSearch();

            }
        }
        else if(counter > 75 && counter <= 675) { // 670
            // режим автосерча
            if(state.equals(Mission.State.NEW)){
                //   прошло 10 секунд с момента как клиенту был отправлен вопрос с предложением запустить режим автосерча, а миссия
                //   по-прежнему в сатусе NEW - значит отменяем ее
                LOGGER.info("Пользователь не дал ответа на переход в режим AUTO_SEARCH в течении заданного времени для миссии : "+getMission().getId()+". Отменяем ее и останавливаем поток");
                cancelMission();
            }else if(state.equals(Mission.State.AUTO_SEARCH)){
                LOGGER.info("Ищу водителя в режиме AUTO_SEARCH для: "+getMission().getId()+" "+counter+" секунд");
            }
        } else if(counter > 675){
            LOGGER.info(String.format("Отменяем миссию с id=%s и останавливаем поток по таймауту", getMission().getId()));
            cancelMission();
        }
        // оповещаем всех водителей о том, что есть новая миссия!
        notifiedDrivers();
    }



    private void sumIncrease(Mission mission, int sum, int sumIncrOrigin){
        mission.getStatistics().setSumIncrease(MoneyUtils.getRubles(sum));
        missionRepository.save(mission);
        MissionFantomDriver missionFantomDriver = missionFantomDriverRepository.findByMission(mission);
        if(missionFantomDriver!=null){
            missionFantomDriver.setSumIncrease(missionFantomDriver.getSumIncrease()+sumIncrOrigin);
            missionFantomDriver.setGeneralTimeSecSearch(counter);
            missionFantomDriverRepository.save(missionFantomDriver);
        }
    }




    private void driverChange(Mission mission) {
        nodeJsNotificationsService.driverChange(mission, "");

        /*
        JSONObject notifClient = new JSONObject();
        try {
           notifClient.put("missionId", mission.getId());
           notifClient.put("arrivalTime", administrationService.calculateArrivalTime(mission, mission.getDriverInfo()));
           ru.trendtech.domain.Location driverLocation = administrationService.getDriverLocation(mission.getDriverInfo());
           double distance = 0;
             if(driverLocation!=null){
                 distance = GeoUtils.distance(mission.getLocationFrom().getLatitude(), mission.getLocationFrom().getLongitude(), driverLocation.getLatitude(), driverLocation.getLongitude());
             }
           notifClient.put("distance", distance);
           socket.notified("driver_changed", notifClient);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        */
    }


    private void clearCurrentMissionForAllFantomDrivers(Mission mission){
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




    private void updateFantomTable(Mission mission){
        MissionFantomDriver missionFantomDriver = missionFantomDriverRepository.findByMission(mission);
          if(missionFantomDriver!=null){
              if(mission.getDriverInfo()!=null && !mission.getDriverInfo().isTypeX()){
                  missionFantomDriver.setDiver(mission.getDriverInfo());
              }
              missionFantomDriver.setGeneralTimeSecSearch(counter);
              missionFantomDriverRepository.save(missionFantomDriver);
          }
    }


    private void searchFantomSupport(Mission mission)  {
        LOGGER.info("MISSION STATE: " + mission.getState().name() + " ");
        if(!EnumSet.of(Mission.State.NEW, Mission.State.AUTO_SEARCH).contains(mission.getState())) {
            // изменился статус миссии, останавливаем поток
            //LOGGER.info(String.format("Статус миссии id= %s изменился. Останавливам поток", getMission().getId()));
               if(!Mission.State.CANCELED.name().equals(mission.getState().name()) && mission.getDriverInfo()!=null && !mission.getDriverInfo().isTypeX()){
                   driverChange(mission);
                   // очищаем текущую миссию в current_mission на всех фантомных водителях
                   clearCurrentMissionForAllFantomDrivers(mission);
                   updateFantomTable(mission);
                   findDriversService.removeThreadSearchFromQueue(getMission().getId(), 0);
                   LOGGER.info("MISSION NOT CANCELED 111");
               } else if(Mission.State.CANCELED.name().equals(mission.getState().name())){
                   // очищаем текущую миссию в current_mission на всех фантомных водителях
                   clearCurrentMissionForAllFantomDrivers(mission);
                   cancelMission();
                   updateFantomTable(mission);
                   LOGGER.info("MISSION CANCELED 222");
               }
//               if(mission.getDriverInfo()!=null && !mission.getDriverInfo().isTypeX()){
//                   // если на миссию назначился не фантомный водитель
//                   findDrivers.removeThreadSearchFromQueue(getMission().getId());xxxx
//               }
        }

        LOGGER.info("Search drivers for mission id: " + getMission().getId() + ". General time search: " + counter);

        if(counter > 0 && counter <= 60) {
            // обычный режим
        }
        else if(counter > 60 && counter <= 120) {
            if(!steps[0]){
                steps[0] = true;
                sumIncrease(mission, mission.getStatistics().getSumIncrease().getAmount().intValue()+50, 50);
            }
        }
        else if(counter > 120 && counter <= 180) {
            if(!steps[1]){
                steps[1] = true;
                sumIncrease(mission, mission.getStatistics().getSumIncrease().getAmount().intValue()+20, 20);
            }
        } else if (counter > 180 && counter <= 240) {
            if (!steps[2]){
                steps[2] = true;
                sumIncrease(mission, mission.getStatistics().getSumIncrease().getAmount().intValue()+30, 30);
            }
        } else if (counter > 240) {
            if (!steps[3]) {
                steps[3] = true;
                cancelFantomMission();
            }
        }
            // оповещаем всех водителей о том, что есть новая миссия!
            notifiedDrivers();
    }




    @Override
    public void run() {

        counter++;
        searchSecond++;
        /*
           здесь происходит поиск водителей в заданном радиусе
           фигачим в нод каждую секунду событие оповещающее водил о том, что есть новая миссия
           фигачим до тех пор, пока либо статус миссии не сменится (кроме автосерча)
        */

        Mission mission = getActualMission(getMission().getId());

        //if(typeSearchDriver.equals(FindDriversService.TypeSearchDriver.FANTOM)){
        //    searchFantomSupport(mission);
        //}else if(typeSearchDriver.equals(FindDriversService.TypeSearchDriver.MULTIPLE)) {
            defaultSearch(mission.getState());
        //}

    }



    private void cancelFantomMission() {

            LOGGER.info(String.format("Отменяем миссию с id=%s и останавливаем поток по таймауту", getMission().getId()));

            findDriversService.removeThreadSearchFromQueue(getMission().getId(), 0);
            clearCurrentMissionForAllFantomDrivers(mission);
            updateFantomTable(getMission());

            /*
            JSONObject cancelJson = new JSONObject();
            cancelJson.put("missionId", getMission().getId());
            cancelJson.put("comment", "Фантом-миссия отменена");
            JSONArray reason = new JSONArray();
            reason.put(1);
            cancelJson.put("reason", reason);
            socket.notified("mission_driver_canceled", cancelJson);
            */

            nodeJsNotificationsService.missionDriverCanceled(mission, "");
            //clientService.cancelMissionByClient(getMission().getId(), getMission().getClientInfo().getId());
            // чистим multiple_id на клиенте если у него НЕТ активных миссий
            //findDrivers.cleaneIfActiveMissionEmpty(getMission());
            //if(!administrationService.sorryMoneyIsTransferToClient(getMission())){
                serviceSMSNotification.sendCustomSMS(mission.getClientInfo().getPhone(), "Водитель отменил заказ. В качестве извинений в Ваш кошелек начислено 100 руб.", "");
                administrationService.operationWithBonusesClient(mission.getClientInfo().getId(), mission.getId(), 100, 20, null, "никто не взял заказ", null);

            //}

    }



    private void cancelMission(){
        findDriversService.removeThreadSearchFromQueue(getMission().getId(), 0);
        clientService.cancelMissionByClient(getMission().getId(), getMission().getClientInfo().getId(), null, ""); // true - отмена фантомным водителем, true - клиентом
        // чистим multiple_id на клиенте если у него НЕТ активных миссий
        findDriversService.cleaneIfActiveMissionEmpty(getMission());
    }



    private void askClientForAutoSearch() {
        nodeJsNotificationsService.askClientForAutoSearch(mission);
         //isAskedClient = true;
    }





    public void buildJsonMessageForDrivers() throws JSONException {
        if(jsonMessageForDriver == null){
            jsonMessageForDriver = new JSONObject();
            jsonMessageForDriver.put("missionId", getMission().getId());
            JSONObject location = new JSONObject();
            location.put("latitude", getMission().getLocationFrom().getLatitude());
            location.put("longitude", getMission().getLocationFrom().getLongitude());
            jsonMessageForDriver.put("location", location);
        }
            this.setJsonMessageForDriver(jsonMessageForDriver);
    }





    private void notifiedDrivers() {
          LOGGER.info("find_drivers - radius: "+radius+" searchSecond: "+searchSecond);
          if(searchSecond > 60){
              searchSecond = 1;
          }
          else if (searchSecond > 0 && searchSecond <= 5) {
              radius = 5;
          }
          else if (searchSecond > 5 && searchSecond <= 10) {
              radius = 7;
          }
          else if (searchSecond > 10 && searchSecond <= 15) {
              radius = 7; // 9
          }
          else if (searchSecond > 15 && searchSecond <= 20) {
              radius = 7; // 12
          }
          else if (searchSecond > 20 && searchSecond <= 23) {
              radius = 7;
          }
          else if (searchSecond > 23 && searchSecond <= 26) {
              radius = 7;
          }
          else if (searchSecond > 26 && searchSecond <= 30) {
              radius = 7;
          }
          try {
              getJsonMessageForDriver().put("count", searchSecond);
              getJsonMessageForDriver().put("radius", radius);
          } catch (JSONException e) {
               e.printStackTrace();
          }
             nodeJsNotificationsService.findDrivers(mission, searchSecond, radius);
    }



    private Mission getActualMission(long missionId){
        return missionRepository.findOne(missionId);
    }


//    private Mission.State getMissionState(long missionId){
//         return missionRepository.findOne(missionId).getState();
//    }


    public MissionFantomDriverRepository getMissionFantomDriverRepository() {
        return missionFantomDriverRepository;
    }

    public void setMissionFantomDriverRepository(MissionFantomDriverRepository missionFantomDriverRepository) {
        this.missionFantomDriverRepository = missionFantomDriverRepository;
    }

    public LocationRepository getDriverLocationRepository() {
        return driverLocationRepository;
    }

    public void setDriverLocationRepository(LocationRepository driverLocationRepository) {
        this.driverLocationRepository = driverLocationRepository;
    }

    public DriverRepository getDriverRepository() {
        return driverRepository;
    }

    public void setDriverRepository(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public AdministrationService getAdministrationService() {
        return administrationService;
    }

    public void setAdministrationService(AdministrationService administrationService) {
        this.administrationService = administrationService;
    }

    public boolean isFantomSupport() {
        return fantomSupport;
    }

    public void setFantomSupport(boolean fantomSupport) {
        this.fantomSupport = fantomSupport;
    }

    public ServiceSMSNotification getServiceSMSNotification() {
        return serviceSMSNotification;
    }

    public void setServiceSMSNotification(ServiceSMSNotification serviceSMSNotification) {
        this.serviceSMSNotification = serviceSMSNotification;
    }

    public boolean isAskedClient() {
        return isAskedClient;
    }

    public void setIsAskedClient(boolean isAskedClient) {
        this.isAskedClient = isAskedClient;
    }

    public JSONObject getJsonMessageForDriver() {
        return jsonMessageForDriver;
    }

    public void setJsonMessageForDriver(JSONObject jsonMessageForDriver) {
        this.jsonMessageForDriver = jsonMessageForDriver;
    }

    public FindDriversService getFindDriversService() {
        return findDriversService;
    }

    public void setFindDriversService(FindDriversService findDriversService) {
        this.findDriversService = findDriversService;
    }

    public MissionRepository getMissionRepository() {
        return missionRepository;
    }

    public void setMissionRepository(MissionRepository missionRepository) {
        this.missionRepository = missionRepository;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ScheduledFuture getScheduledFuture() {
        return scheduledFuture;
    }

    public void setScheduledFuture(ScheduledFuture scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public String getPrintInfo() {
        return printInfo;
    }

    public void setPrintInfo(String printInfo) {
        this.printInfo = printInfo;
    }

    public NodeJsNotificationsService getNodeJsNotificationsService() {
        return nodeJsNotificationsService;
    }

    public void setNodeJsNotificationsService(NodeJsNotificationsService nodeJsNotificationsService) {
        this.nodeJsNotificationsService = nodeJsNotificationsService;
    }
}
