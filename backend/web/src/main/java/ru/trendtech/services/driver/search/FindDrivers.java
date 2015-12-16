package ru.trendtech.services.driver.search;

import org.json.JSONException;
import ru.trendtech.domain.Mission;
import ru.trendtech.domain.MultipleMission;
import ru.trendtech.domain.courier.Order;

import java.util.concurrent.ExecutionException;

/**
 * Created by petr on 16.07.2015.
 */
public interface FindDrivers {
    void findMultipleDrivers(MultipleMission multipleMission) throws ExecutionException, InterruptedException, JSONException;

    void findDriversWhenMissionWithFantomDriver(Mission mission) throws ExecutionException, InterruptedException, JSONException;

    void findDriversWhenDriverCanceledMission(Mission mission) throws ExecutionException, InterruptedException, JSONException;

    void findCourier(Order order);

    void clearCurrentMissionForAllFantomDrivers(Mission mission);

    void updateFantomTable(Mission mission, int secSearch);

    StartSearch createThreadSearch(Mission mission, Order order, StartSearch startSearch);

    void printQueueMission() throws ExecutionException, InterruptedException;

    boolean isRun(Mission mission, Order order);

    void askClientForAutoSearch(Mission mission);

    void removeThreadSearchFromQueue(long missionId, long orderId);

    void cleaneIfActiveMissionEmpty(Mission mission);

    void sumIncrease(Mission mission, int sum, int sumIncrOrigin, int secSearch);

    void driverChange(Mission mission);

    void cancelMission(FindDriversService.SearchMode searchMode, Mission mission, int secSearch, long reasonId, String ip) throws JSONException;

    void cancelOrder(Order order, int secSearch, long reasonId);

    void findDrivers(Mission mission, int secSearch, float radius);

    void findCouriers(Order order, int secSearch);

    Mission getActualMission(long missionId);

    Order getActualOrder(long orderId);

}
