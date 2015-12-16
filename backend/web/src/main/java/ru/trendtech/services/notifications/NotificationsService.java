package ru.trendtech.services.notifications;

import ru.trendtech.domain.DriverLocation;
import ru.trendtech.domain.Location;
import ru.trendtech.domain.Mission;
import ru.trendtech.domain.courier.Order;

/**
 * File created by petr on 12/06/2015 1:41.
 */
public interface NotificationsService {

    /* courier */

    void cancelOrder(Order order);

    void courierOrderSumChanged(Order order);

    // заказ взят оператором в обработку
    void inProgressByOperator(Order order);

    void readyToProgress(Order order);

    void courierOrderFinished(Order order);

    void courierClientCancelOrder(Order order);

    // отмена заказа через АРМ
    void courierOrderCanceledToClientAndCourier(Order order);

    void courierSendLocationToClient(Order order,  DriverLocation location);

    void courierOrderFired(Order order, int countNotified);

    void courierOrderConfirm(Order order);

    void courierNewOrderDispatcher(Order order);

    void findCouriers(Order order, int count);

    void courierLate(Order order, int time);

    void courierOrderAssign(Order order, int arrivalTime, DriverLocation location);

    /* все купил(забрал) и едет к клиенту */
    void courierAllPurchased(Order order);

    /* courier: сервис успешно активирован */
    void serviceSuccessfullyActivated(long clientId, String message);

    /* courier */

    void openMissionCard(long missionId);

    void bookedMissionFired(long missionId, String time);

    void bookedMissionFailed(long missionId);

    void driverLate();

    void driverLocation(Mission mission, DriverLocation driverLocation);

    /* ребут водилы */
    void driverRefresh(long driverId, long webUserId);

    /* отмена автопоиска */
    void autosearchCanceled(long missionId);

    /* передача заказа */
    void transferMissionToDriver(long missionId, long from_driverId, long to_driverId, String state, boolean booked);

    /* взятие второго заказа водителем*/
    void driverAssigneSecondOrder(Mission mission, long driverId, int arrivalTime, DriverLocation location);

    /* оповещаем о взятии заказа */
    void missionAssigneMsg(Mission mission, long driverId, int arrivalTime, DriverLocation location, String message);

    /* водитель прибыл на заказ */
    void driverArrived(Mission mission, boolean booked, String message);

    /* произвольное сообщение водителю */
    void driverCustomMessage(long driverId, String message);

    /* произвольное сообщение клиенту */
    void clientCustomMessage(long clientId, String message);

    /* произвольное сообщение водителю через АРМ */
    void driverCustomMessageARM(long driverId, String text);

    /* сообщать водителю о том, что он выехал или въехал в зону межгорода */
    void regionChange(long driverId, String message, boolean fromRemote);

    void sendMissionPaymentResult(long missionId, boolean result, String answer);

    /* ответ клиента на предложение оплатить поездку по карте */
    void paymentCardAnswer(long missionId, boolean answer);

    /* предложение оплатить поездку по карте */
    void askClientForCardPayment(long missionId, int sum);

    /* миссия недоступна */
    void missionBecameUnavailable(long missionId, long driverId);

    /* отмена второго заказа в очереди */
    void missionCancelSecondOrder(long missionId, long driverId);

    /* уведомление о начале платного ожидания. */
    void missionWaitPaymentStart(long clientId, String message);

    /* поиск водителя */
    void findDrivers(Mission mission, int count, float radius);

    /* сменился водитель */
    void driverChange(Mission mission, String message);

    /* водитель отменил миссию */
    void missionDriverCanceled(Mission mission, String message);

    /* предложить клиенту переход в режим автопоиска */
    void askClientForAutoSearch(Mission mission);

}






/*

    PUSH - old!

    void newMissionAvailable(long missionId);

    void driverLate(long driverId, int lateTime);

    void driverAssigned(long driverId, long missionId, int arrivalTime, boolean booked);

    void cancelMissionByClient(long missionId);

    void cancelMissionByDriver(long missionId);

    void driverChangedLocation(long driverId, ItemLocation location);

    void driverArrived(long driverId, int freeTime);

    void missionPauseStart(long missionId, ItemLocation location);

    void missionPauseEnd(long missionId, ItemLocation location);

    void missionStart(long missionId);

    void missionEnd(long missionId);

    void missionPayment(long missionId);

    void clientReadyToGo(long missionId, int expectedTime);

    void availableWiFi(long missionId, String networkId, String password);

    void bookedMissionConfirm(Long missionId);
    */