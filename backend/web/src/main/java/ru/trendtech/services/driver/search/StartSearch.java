package ru.trendtech.services.driver.search;

import org.slf4j.LoggerFactory;
import ru.trendtech.domain.Mission;
import ru.trendtech.domain.courier.Order;
import java.util.EnumSet;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by petr on 16.07.2015.
 */
public class StartSearch implements Runnable {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(StartSearch.class);
    private boolean steps[] = {false, false, false, false};
    private int searchSecond = 0; // seconds of search
    public static boolean isAskedClient = false;
    private String description;
    private int counter = 0;
    private float radius;
    private Mission mission;
    private Order order;
    private ScheduledFuture<StartSearch> scheduledFuture;
    private FindDriversService findDriversService;
    private FindDriversService.SearchMode searchMode;


    @Override
    public void run() {
        counter++;
        searchSecond++;

        setCounter(counter);
        setSearchSecond(searchSecond);


        if(getMission() != null){
            Mission mission = findDriversService.getActualMission(getMission().getId());
            setMission(mission);
        }
        if(getOrder() != null){
            Order order = findDriversService.getActualOrder(getOrder().getId());
            setOrder(order);
        }

        if(getSearchMode().equals(FindDriversService.SearchMode.SEARCH_DRIVER_WHEN_MISSION_WITH_FANTOM)){
            searchFantomSupport();
        } else if(getSearchMode().equals(FindDriversService.SearchMode.SEARCH_DRIVER_WHEN_MISSION_MULTIPLE_SUPPORT)){
            searchWithMultipleSupport();
        } else if(getSearchMode().equals(FindDriversService.SearchMode.SEARCH_DRIVER_WHEN_MISSION_CANCELED_BY_DRIVER)){
            searchWhenMissionCanceledByDriver();
        } else if(getSearchMode().equals(FindDriversService.SearchMode.SEARCH_COURIER)) {
            searchCourier();
        }
    }



    private void searchCourier(){
        Order.State state = getOrder().getState();
        if (!EnumSet.of(Order.State.CONFIRMED).contains(state)) {
            findDriversService.removeThreadSearchFromQueue(0, getOrder().getId());
        }
        LOGGER.info("Search couriers for order id: " + getOrder().getId() + ". General time search: " + getCounter());
        if (getCounter() > 600) {
            LOGGER.info("Отмена заказа по таймауту");
            findDriversService.cancelOrder(getOrder(), getCounter(), 7);
        }
            // оповещаем всех курьеров о том, что есть новый заказ!
            notifiedDrivers(false);
    }



    private void searchWhenMissionCanceledByDriver(){
        if(!EnumSet.of(Mission.State.NEW, Mission.State.AUTO_SEARCH).contains(getMission().getState())) {
            // изменился статус миссии, останавливаем поток
            if(!Mission.State.CANCELED.name().equals(getMission().getState().name()) && getMission().getDriverInfo()!=null && !getMission().getDriverInfo().isTypeX()){
                findDriversService.driverChange(getMission());
                findDriversService.removeThreadSearchFromQueue(getMission().getId(), 0);
                LOGGER.info("SearchWhenMissionCanceledByDriver: DRIVER CHANGE");
            } else
            if(Mission.State.CANCELED.name().equals(getMission().getState().name())){
                // очищаем текущую миссию в current_mission на всех фантомных водителях
                findDriversService.removeThreadSearchFromQueue(getMission().getId(), 0);
                //findDriversService.cancelMission(FindDriversService.SearchMode.WHEN_MISSION_WITH_FANTOM, getMission(), getCounter());
                LOGGER.info("SearchWhenMissionCanceledByDriver: MISSION CANCELED");
            }
        }

        LOGGER.info("Search drivers for mission id: " + getMission().getId() + ". General time search: " + getCounter());
        if (getCounter() > 60) {
             findDriversService.cancelMission(FindDriversService.SearchMode.SEARCH_DRIVER_WHEN_MISSION_CANCELED_BY_DRIVER, getMission(), getCounter(), 7, "");
        }
             // оповещаем всех водителей о том, что есть новая миссия!
             notifiedDrivers(true);
    }









    private void searchWithMultipleSupport(){
        if(!EnumSet.of(Mission.State.NEW, Mission.State.AUTO_SEARCH).contains(getMission().getState())) {
            // изменился статус миссии, останавливаем поток
            LOGGER.info(String.format("Статус миссии id= %s изменился. Останавливам поток", getMission().getId()));

            findDriversService.removeThreadSearchFromQueue(getMission().getId(), 0);
            // чистим multiple_id на клиенте если у него НЕТ активных миссий
            findDriversService.cleaneIfActiveMissionEmpty(getMission());
        }

        if(counter > 0 && counter <= 60) {
            // обычный режим
            LOGGER.info("Ищу водилу в обычном режиме для: " + getMission().getId() + " "+counter+" секунд");
        }
        else if(counter > 60 && counter <= 75) {
            // отсылаем клиенту сообщение: Вы хотите продолжить поиск в режиме автосерча [ждем ответа 10 секунд]
            LOGGER.info("Отправлено ли событие на нод с предложением перейти в AUTO_SEARCH: " + (isAskedClient ? "ДА":"НЕТ"));
            if(isAskedClient){
                // уже спроил у клиента по поводу перехода в режим автопоиска
                LOGGER.info("Ждем ответа пользователя для включения рнжима AUTO_SEARCH для: " + getMission().getId());
            }else{
                LOGGER.info("Вы хотите перейти в режим автопоиска для миссии: " + getMission().getId());
                findDriversService.askClientForAutoSearch(getMission());
            }
        }
        else if(counter > 75 && counter <= 675) { // 670
            // режим автосерча
            if(getMission().getState().equals(Mission.State.NEW)){
                //   прошло 10 секунд с момента как клиенту был отправлен вопрос с предложением запустить режим автосерча, а миссия
                //   по-прежнему в сатусе NEW - значит отменяем ее
                LOGGER.info("Пользователь не дал ответа на переход в режим AUTO_SEARCH в течении заданного времени для миссии : " + getMission().getId() + ". Отменяем ее и останавливаем поток");
                findDriversService.cancelMission(FindDriversService.SearchMode.SEARCH_DRIVER_WHEN_MISSION_MULTIPLE_SUPPORT, getMission(), getCounter(), 6, "");
            }else if(getMission().getState().equals(Mission.State.AUTO_SEARCH)){
                LOGGER.info("Ищу водителя в режиме AUTO_SEARCH для: "+getMission().getId()+" "+getCounter()+" секунд");
            }
        } else if(counter > 675){
            LOGGER.info(String.format("Отменяем миссию с id=%s и останавливаем поток по таймауту", getMission().getId()));
            findDriversService.cancelMission(FindDriversService.SearchMode.SEARCH_DRIVER_WHEN_MISSION_MULTIPLE_SUPPORT, getMission(), getCounter(), 7, "");
        }
            // оповещаем всех водителей о том, что есть новая миссия!
            notifiedDrivers(true);
    }






    private void searchFantomSupport()  {
        if(!EnumSet.of(Mission.State.NEW, Mission.State.AUTO_SEARCH).contains(getMission().getState())) {
            // изменился статус миссии, останавливаем поток
            if(!Mission.State.CANCELED.name().equals(getMission().getState().name()) && getMission().getDriverInfo()!=null && !getMission().getDriverInfo().isTypeX()){
                findDriversService.driverChange(getMission());
                // очищаем текущую миссию в current_mission на всех фантомных водителях
                findDriversService.clearCurrentMissionForAllFantomDrivers(getMission());
                findDriversService.updateFantomTable(getMission(), getCounter());
                findDriversService.removeThreadSearchFromQueue(getMission().getId(), 0);
                LOGGER.info("DRIVER CHANGE");
            } else
            if(Mission.State.CANCELED.name().equals(getMission().getState().name())){
                // очищаем текущую миссию в current_mission на всех фантомных водителях
                findDriversService.clearCurrentMissionForAllFantomDrivers(getMission());
                findDriversService.updateFantomTable(getMission(), getCounter());
                findDriversService.removeThreadSearchFromQueue(getMission().getId(), 0);
                //findDriversService.cancelMission(FindDriversService.SearchMode.WHEN_MISSION_WITH_FANTOM, getMission(), getCounter());
                LOGGER.info("MISSION CANCELED");
            }
        }

        LOGGER.info("Search drivers for mission id: " + getMission().getId() + ". General time search: " + getCounter());

        if(getCounter() > 0 && getCounter() <= 60) {
            // обычный режим
        } else if(getCounter() > 60 && getCounter() <= 120) {
            if(!steps[0]){
                steps[0] = true;
                findDriversService.sumIncrease(getMission(), getMission().getStatistics().getSumIncrease().getAmount().intValue() + 50, 50, getCounter());
            }
        } else if(getCounter() > 120 && getCounter() <= 180) {
            if(!steps[1]){
                steps[1] = true;
                findDriversService.sumIncrease(getMission(), getMission().getStatistics().getSumIncrease().getAmount().intValue() + 20, 20, getCounter());
            }
        } else if (getCounter() > 180 && getCounter() <= 240) {
            if (!steps[2]){
                steps[2] = true;
                findDriversService.sumIncrease(getMission(), getMission().getStatistics().getSumIncrease().getAmount().intValue() + 30, 30, getCounter());
            }
        } else if (getCounter() > 240) {
            if (!steps[3]) {
                steps[3] = true;
                findDriversService.cancelMission(FindDriversService.SearchMode.SEARCH_DRIVER_WHEN_MISSION_WITH_FANTOM, getMission(), getCounter(), 1, "");
            }
        }
                // оповещаем всех водителей о том, что есть новая миссия!
                notifiedDrivers(true);
    }




    public void notifiedDrivers(boolean byMission) {
        if(getSearchSecond() > 60){
            setSearchSecond(1);
        }
        else if (getSearchSecond() > 0 && getSearchSecond() <= 5) {
            setRadius(5);
        }
        else if (getSearchSecond() > 5 && getSearchSecond() <= 10) {
            setRadius(7);
        }
        else if (getSearchSecond() > 10 && getSearchSecond() <= 15) {
            setRadius(7); // 9
        }
        else if (getSearchSecond() > 15 && getSearchSecond() <= 20) {
            setRadius(7); // 12
        }
        else if (getSearchSecond() > 20 && getSearchSecond() <= 23) {
            setRadius(7);
        }
        else if (getSearchSecond() > 23 && getSearchSecond() <= 26) {
            setRadius(7);
        }
        else if (getSearchSecond() > 26 && getSearchSecond() <= 30) {
            setRadius(7);
        }
        if(byMission){
            findDriversService.findDrivers(getMission(), getSearchSecond(), getRadius());
        } else {
            findDriversService.findCouriers(getOrder(), getSearchSecond());
        }
    }



    public int getSearchSecond() {
        return searchSecond;
    }

    public void setSearchSecond(int searchSecond) {
        this.searchSecond = searchSecond;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ScheduledFuture<StartSearch> getScheduledFuture() {
        return scheduledFuture;
    }

    public void setScheduledFuture(ScheduledFuture<StartSearch> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

    public FindDriversService getFindDriversService() {
        return findDriversService;
    }

    public void setFindDriversService(FindDriversService findDriversService) {
        this.findDriversService = findDriversService;
    }

    public FindDriversService.SearchMode getSearchMode() {
        return searchMode;
    }

    public void setSearchMode(FindDriversService.SearchMode searchMode) {
        this.searchMode = searchMode;
    }
}
