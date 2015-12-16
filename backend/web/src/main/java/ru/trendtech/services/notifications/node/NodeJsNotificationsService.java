package ru.trendtech.services.notifications.node;

import org.joda.time.Minutes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.trendtech.common.mobileexchange.model.common.ItemLocation;
import ru.trendtech.domain.DriverLocation;
import ru.trendtech.domain.Location;
import ru.trendtech.domain.Mission;
import ru.trendtech.domain.courier.Order;
import ru.trendtech.domain.courier.OrderAddress;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.notifications.NotificationsService;
import ru.trendtech.utils.DateTimeUtils;
import ru.trendtech.utils.GeoUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by petr on 18.06.2015.
 */
@Service(value = "node")
public class NodeJsNotificationsService implements NotificationsService {
    @Autowired
    private NodeJsService socket;
    @Autowired
    private AdministrationService administrationService;
    @Autowired
    private CommonService commonService;


    @Override
    public void courierOrderSumChanged(Order order) {
        JSONObject json = new JSONObject();
        try {
            json.put("security_token", order.getDriver().getToken());
            json.put("orderId", order.getId());
            json.put("courierId", order.getDriver().getId());
            socket.notified("courier_order_sum_changed", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void openMissionCard(long missionId) {
        JSONObject json = new JSONObject();
        try {
            json.put("missionId", missionId);
            socket.notified("open_mission_card", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void bookedMissionFired(long missionId, String time) {
        JSONObject json = new JSONObject();
        try {
            json.put("missionId", missionId);
            json.put("bookedTime", time);
            socket.notified("booked_mission_fired", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bookedMissionFailed(long missionId) {
        JSONObject json = new JSONObject();
        try {
            json.put("missionId", missionId);
            socket.notified("booked_mission_failed", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void readyToProgress(Order order) {
        JSONObject json = new JSONObject();
        try {
            json.put("security_token", order.getDriver().getToken());
            json.put("orderId", order.getId());
            socket.notified("courier_going_to_order", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void cancelOrder(Order order) {

    }

    @Override
    public void courierOrderFinished(Order order) {
        JSONObject json = new JSONObject();
        try {
            json.put("security_token", order.getDriver() != null ? order.getDriver().getToken() : "");
            json.put("orderId", order.getId());
            socket.notified("courier_order_finished", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void driverLate() {

    }



    // отмена заказа клиентом
    @Override
    public void courierClientCancelOrder(Order order) {
        JSONObject json = new JSONObject();
        try {
            json.put("security_token", order.getClient().getToken());
            json.put("orderId", order.getId());
            json.put("courierId", order.getDriver()!=null ? order.getDriver().getId(): 0);
            socket.notified("courier_client_cancel_order", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void courierOrderCanceledToClientAndCourier(Order order) {
        // socket.emit('courier_order_canceled_to_client_and_courier', {'security_token': 'dfsklgjdflkdfkblknbcvngkgk',  'orderId': 203, 'courierId': 22});
        JSONObject json = new JSONObject();
        try {
            json.put("security_token", order.getDriver() != null ? order.getDriver().getToken() : "");
            json.put("orderId", order.getId());
            json.put("courierId", order.getDriver() != null ? order.getDriver().getId(): 0);
            socket.notified("courier_order_canceled_to_client_and_courier", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void courierOrderFired(Order order, int countNotified) {
        //socket.emit('courier_order_fired',{'security_token', 'dgfflgjfklgfkgjgjjj', 'orderId': 12444, 'courierId': 5, 'countNotified': 1});
        JSONObject json = new JSONObject();
        try {
            json.put("security_token", order.getDriver().getToken());
            json.put("orderId", order.getId());
            json.put("courierId", order.getDriver().getId());
            json.put("countNotified", countNotified);
            socket.notified("courier_order_fired", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void courierOrderConfirm(Order order) {
        //socket.emit('courier_order_confirm', {'security_token': 'dgjflkjgflkhjgkljlfjlfgjlfjf', 'orderId': 12444, 'courierId': 5});
        JSONObject json = new JSONObject();
        try {
            json.put("security_token", order.getClient().getToken());
            json.put("orderId", order.getId());
            json.put("courierId", order.getDriver() != null ? order.getDriver().getId() : 0);
            socket.notified("courier_order_confirm", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void courierNewOrderDispatcher(Order order) {
        // socket.emit('courier_new_order_dispatcher', {'orderId': 1, 'clientId': 5, 'security_token': 'dkjfdlkjgldfkjgfklhjgkgjhfvjfjhj'});
        JSONObject json = new JSONObject();
        try {
            json.put("security_token", order.getClient().getToken());
            json.put("orderId", order.getId());
            json.put("clientId", order.getClient().getId());
            socket.notified("courier_new_order_dispatcher", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void findCouriers(Order order, int count) {
        // socket.emit('find_couriers', {'security_token': 'dfsklgjdflkdfkblknbcvngkgk',  'orderId': 20, 'count': 2});
        JSONObject json = new JSONObject();
        try {
            json.put("security_token", order.getClient().getToken());
            json.put("orderId", order.getId());
            json.put("count", count);
            socket.notified("find_couriers", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void courierLate(Order order, int time) {
        // ('courier_late', {'security_token': 'dgjflkjgflkhjgkljlfjlfgjlfjf', 'orderId': 12444, 'courierId': 5, 'time': 10});
        JSONObject json = new JSONObject();
        try {
            json.put("security_token", order.getDriver().getToken());
            json.put("orderId", order.getId());
            json.put("courierId", order.getDriver().getId());
            json.put("time", time);
            socket.notified("courier_late", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void inProgressByOperator(Order order) {

    }

    @Override
    public void courierAllPurchased(Order order) {
        // 'courier_all_purchased', {'orderId': 12444, 'courierId': 5, 'security_token': 'dkjfdlkjgldfkjgfklhjgkgjhfvjfjhj'}
        JSONObject json = new JSONObject();
        try {
            json.put("security_token", order.getDriver().getToken());
            json.put("orderId", order.getId());
            json.put("courierId", order.getDriver().getId());
            socket.notified("courier_all_purchased", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void courierOrderAssign(Order order, int arrivalTime, DriverLocation location) {
        // socket.emit('courier_order_assigned', {'security_token': 'fce6ac32067a27a88c043041129dba02', 'orderId': 12444, 'arrivalTime': 5, 'location':{ 'distance': 0,'longitude': 82.93843841552734, 'latitude': 55.04728698730469, 'angle': -90, 'courierId': 23 }});
        JSONObject json = new JSONObject();
        try {
            JSONObject locationJson = new JSONObject();
            json.put("security_token", order.getDriver().getToken());
            json.put("orderId", order.getId());
            json.put("arrivalTime", arrivalTime);
            locationJson.put("latitude", location.getLocation().getLatitude());
            locationJson.put("longitude", location.getLocation().getLongitude());
            locationJson.put("courierId", order.getDriver().getId());

            ItemLocation itemLocation = commonService.getLocationByFirstOrderAddress(order.getTargetAddresses());

            locationJson.put("distance", GeoUtils.distance(location.getLocation().getLatitude(), location.getLocation().getLongitude(), itemLocation.getLatitude(), itemLocation.getLongitude()));
            locationJson.put("angle", location.getAngle());
            json.put("location", locationJson);
            socket.notified("courier_order_assigned", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void courierSendLocationToClient(Order order, DriverLocation location) {
        // socket.emit('courier_send_location_to_client', {'orderId': 12444, 'courierId': 5, 'location':{ 'distance': 0,'longitude': 82.93843841552734, 'latitude': 55.04728698730469, 'angle': -90, 'courierId': 23 }});
        JSONObject json = new JSONObject();
        try {
            JSONObject locationJson = new JSONObject();
            json.put("security_token", order.getClient().getToken());
            json.put("orderId", order.getId());
            json.put("courierId", order.getDriver().getId());
            locationJson.put("latitude", location.getLocation().getLatitude());
            locationJson.put("longitude", location.getLocation().getLongitude());
            locationJson.put("courierId", order.getDriver().getId());

            ItemLocation itemLocation = commonService.getLocationByFirstOrderAddress(order.getTargetAddresses());

            locationJson.put("distance", GeoUtils.distance(location.getLocation().getLatitude(), location.getLocation().getLongitude(), itemLocation.getLatitude(), itemLocation.getLongitude()));
            locationJson.put("angle", location.getAngle());
            json.put("location", locationJson);
            socket.notified("courier_send_location_to_client", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void driverLocation(Mission mission, DriverLocation driverLocation) {
        JSONObject json = new JSONObject();
        try {
        JSONObject locationJson = new JSONObject();
        locationJson.put("latitude", driverLocation.getLocation().getLatitude());
        locationJson.put("longitude", driverLocation.getLocation().getLongitude());
        locationJson.put("driverId", driverLocation.getDriver().getId());
        locationJson.put("distance", GeoUtils.distance(driverLocation.getLocation().getLatitude(), driverLocation.getLocation().getLongitude(), mission.getLocationFrom().getLatitude(), mission.getLocationFrom().getLongitude()));
        locationJson.put("angle", -90);
        locationJson.put("status", "хз");
        json.put("location", locationJson);
        json.put("missionId", mission.getId());
            socket.notified("driver_location", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    /* - Zavezu - */

    @Override
    public void serviceSuccessfullyActivated(long clientId, String message) {

    }

    /* - Zavezu - */


    @Override
    public void driverRefresh(long driverId, long webUserId) {
        JSONObject json = new JSONObject();
        try {
            json.put("driverId", driverId);
            json.put("webUserId", webUserId);
            socket.notified("driver_refresh", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void autosearchCanceled(long missionId) {
        JSONObject json = new JSONObject();
        try {
            json.put("missionId", missionId);
            socket.notified("autosearch_canceled", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void missionAssigneMsg(Mission mission, long driverId, int arrivalTime, DriverLocation location, String message) {
        JSONObject json = new JSONObject();
        try {
            JSONObject locationJson = new JSONObject();
            json.put("missionId", mission.getId());
            json.put("arrivalTime", arrivalTime);
            locationJson.put("latitude", location.getLocation().getLatitude());
            locationJson.put("longitude", location.getLocation().getLongitude());
            locationJson.put("driverId", driverId);
            locationJson.put("distance", GeoUtils.distance(location.getLocation().getLatitude(), location.getLocation().getLongitude(), mission.getLocationFrom().getLatitude(), mission.getLocationFrom().getLongitude()));
            locationJson.put("angle", -90);
            json.put("location", locationJson);
            socket.notified("mission_assign_msg", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void driverAssigneSecondOrder(Mission mission, long driverId, int arrivalTime, DriverLocation location) {
        JSONObject json = new JSONObject();
        try {
            JSONObject locationJson = new JSONObject();
            json.put("missionId", mission.getId());
            json.put("arrivalTime", arrivalTime);
            locationJson.put("latitude", location.getLocation().getLatitude());
            locationJson.put("longitude", location.getLocation().getLongitude());
            locationJson.put("driverId", driverId);
            locationJson.put("distance", GeoUtils.distance(location.getLocation().getLatitude(), location.getLocation().getLongitude(), mission.getLocationFrom().getLatitude(), mission.getLocationFrom().getLongitude()));
            locationJson.put("angle", -90);
            json.put("location", locationJson);
            socket.notified("mission_assign_second_order", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clientCustomMessage(long clientId, String message) {

    }

    @Override
    public void findDrivers(Mission mission, int count, float radius) {
        JSONObject json = new JSONObject();
        try {
            json.put("missionId", mission.getId());
            json.put("count", count);
            json.put("radius", radius);
            JSONObject location = new JSONObject();
            location.put("latitude", mission.getLocationFrom().getLatitude());
            location.put("longitude", mission.getLocationFrom().getLongitude());
            json.put("location", location);
            socket.notified("find_drivers", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void missionDriverCanceled(Mission mission, String message) {
        JSONObject json = new JSONObject();
        try {
            json.put("missionId", mission.getId());
            json.put("comment", "Миссия отменена водителем");
            JSONArray reason = new JSONArray();
            reason.put(1);
            json.put("reason", reason);
            socket.notified("mission_driver_canceled", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void askClientForAutoSearch(Mission mission) {
        JSONObject json = new JSONObject();
        try {
            json.put("missionId", mission.getId());
            socket.notified("ask_client_for_autosearch", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void missionWaitPaymentStart(long clientId, String message) {

    }




    @Override
    public void askClientForCardPayment(long missionId, int sum) {
        JSONObject json = new JSONObject();
        try {
            json.put("missionId", missionId);
            json.put("amount", sum);
            socket.notified("ask_client_for_card_payment", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void sendMissionPaymentResult(long missionId, boolean result, String answer) {
        JSONObject json = new JSONObject();
        try {
            json.put("missionId", missionId);
            json.put("result", result);
            json.put("answer", answer);
            socket.notified("send_mission_payment_result", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void paymentCardAnswer(long missionId, boolean answer) {
        JSONObject json = new JSONObject();
        try {
          json.put("missionId", missionId);
          json.put("agree", answer);
          socket.notified("send_client_card_payment_decision", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void missionCancelSecondOrder(long missionId, long driverId) {
        JSONObject json = new JSONObject();
        try {
            json.put("missionId", driverId);
            json.put("driverId", driverId);
            socket.notified("mission_cancel_second_order", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void missionBecameUnavailable(long missionId, long driverId) {
        JSONObject json = new JSONObject();
        try {
            json.put("missionId", missionId);
            json.put("driverId", driverId);
            socket.notified("mission_became_unavailable", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void driverCustomMessageARM(long driverId, String message) {
        JSONObject json = new JSONObject();
        try {
            json.put("driverId", driverId);
            json.put("text", message);
            socket.notified("driver_message_arm", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void driverCustomMessage(long driverId, String message) {
        JSONObject json = new JSONObject();
        try {
            json.put("driverId", driverId);
            json.put("text", message);
            socket.notified("driver_message", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void driverArrived(Mission mission, boolean booked, String message) {
        JSONObject json = new JSONObject();
        try {
            json.put("driverId", mission.getDriverInfo().getId());
            json.put("missionId", mission.getId());
            socket.notified("mission_arrived", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void regionChange(long driverId, String message, boolean fromRemote) {
        JSONObject json = new JSONObject();
        try {
            json.put("driverId", driverId);
            json.put("message", message);
            json.put("fromRemote", fromRemote);
            socket.notified("server_region_change", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void transferMissionToDriver(long missionId, long from_driverId, long to_driverId, String state, boolean booked) {
        JSONObject json = new JSONObject();
        try {
            json.put("missionId", missionId);
            json.put("from_driverId", from_driverId);
            json.put("to_driverId", to_driverId);
            json.put("state", state);
            json.put("booked", booked);
            socket.notified("transfer_mission_to_driver", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void driverChange(Mission mission, String message) {
        JSONObject notifClient = new JSONObject();
        try {
            notifClient.put("missionId", mission.getId());
            int timeOfArrival = 0;
            if(mission.isBooked()){
                if(DateTimeUtils.nowNovosib_GMT6().isBefore(mission.getTimeOfStarting())){
                    // время старта заказа в будущем
                    Minutes minutes = Minutes.minutesBetween(DateTimeUtils.nowNovosib_GMT6(), mission.getTimeOfStarting());
                    timeOfArrival = Math.abs(minutes.getMinutes());
                }
            } else{
                    timeOfArrival =  commonService.calculateArrivalTime(mission, null, mission.getDriverInfo());
            }
            notifClient.put("arrivalTime", timeOfArrival);
            ru.trendtech.domain.Location driverLocation = administrationService.getDriverLocation(mission.getDriverInfo());
            double distance = 0;
            if (driverLocation != null) {
                distance = GeoUtils.distance(mission.getLocationFrom().getLatitude(), mission.getLocationFrom().getLongitude(), driverLocation.getLatitude(), driverLocation.getLongitude());
            }
            notifClient.put("distance", distance);
            socket.notified("driver_changed", notifClient);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
