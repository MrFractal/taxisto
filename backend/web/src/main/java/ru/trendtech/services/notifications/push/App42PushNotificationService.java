package ru.trendtech.services.notifications.push;

import com.shephertz.app42.paas.sdk.java.App42API;
import com.shephertz.app42.paas.sdk.java.App42Exception;
import com.shephertz.app42.paas.sdk.java.push.PushNotificationService;
import com.shephertz.app42.paas.sdk.java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.trendtech.common.mobileexchange.model.common.CustomException;
import ru.trendtech.domain.*;
import ru.trendtech.domain.courier.Order;
import ru.trendtech.domain.courier.OrderAddress;
import ru.trendtech.domain.courier.TargetAddressState;
import ru.trendtech.repositories.ClientRepository;
import ru.trendtech.repositories.MissionRepository;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.notifications.NotificationsService;
import ru.trendtech.utils.DeviceUtil;

import javax.annotation.PostConstruct;
import java.util.HashMap;

/**
 * Created by petr on 07.07.2015.
 */
@Service(value = "push")
public class App42PushNotificationService implements NotificationsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(App42PushNotificationService.class);
    @Autowired
    private ClientRepository clientRepository;
    @Value("${push.api.key}")
    private String apiKey;
    @Value("${push.secret.key}")
    private String secretKey;
    private PushNotificationService pushNotificationService;


    @PostConstruct
    public void initPush(){
        LOGGER.info("~~~~~~ App42PushNotificationService initialise ~~~~~~~");
        HashMap<String, String> otherMetaHeaders = new HashMap();
        otherMetaHeaders.put("dataEncoding", "true");
        App42API.initialize(apiKey, secretKey);
        pushNotificationService = App42API.buildPushNotificationService();
        pushNotificationService.setOtherMetaHeaders(otherMetaHeaders);
    }



    @Override
    public void sendMissionPaymentResult(long missionId, boolean result, String answer) {

    }


    /* - courier - */

    @Override
    public void courierOrderSumChanged(Order order) {

    }



    @Override
    public void readyToProgress(Order order) {
        try {
            Client client = order.getClient();
            DeviceInfo deviceInfo = DeviceUtil.getDeviceInfo(client.getDevices());
            if(deviceInfo!=null && !StringUtils.isEmpty(deviceInfo.getPushId())){
                pushNotificationService.sendPushMessageToDevice(resolveDeviceType(client), deviceInfo.getPushId(), "Ваш курьер начал выполнение заказа");
            }
        } catch(App42Exception exception){
            LOGGER.info("exception in readyToProgress: " + exception.getMessage());
        }
    }




    @Override
    public void inProgressByOperator(Order order) {
        try {
            Client client = order.getClient();
            DeviceInfo deviceInfo = DeviceUtil.getDeviceInfo(client.getDevices());
            if(deviceInfo!=null && !StringUtils.isEmpty(deviceInfo.getPushId())){
                 pushNotificationService.sendPushMessageToDevice(resolveDeviceType(client), deviceInfo.getPushId(), "Заказ взят оператором в обработку"); // Заказ взят оператором в обработку
            }
        } catch(App42Exception exception){
            LOGGER.info("exception in inProgressByOperator: " + exception.getMessage());
            //exception.printStackTrace();
        }
    }




    @Override
    public void courierOrderFinished(Order order) {
        try {
            Client client = order.getClient();
            DeviceInfo deviceInfo = DeviceUtil.getDeviceInfo(client.getDevices());
            if(deviceInfo!=null && !StringUtils.isEmpty(deviceInfo.getPushId())){
                pushNotificationService.sendPushMessageToDevice(resolveDeviceType(client), deviceInfo.getPushId(), "Заказ завершен");
            }
        } catch(App42Exception exception){
            //exception.printStackTrace();
            LOGGER.info("exception in courierOrderFinished: " + exception.getMessage());
        }
    }


    @Override
    public void courierClientCancelOrder(Order order) {

    }



    @Override
    public void courierOrderCanceledToClientAndCourier(Order order) {
        try {
            Client client = order.getClient();
            DeviceInfo deviceInfo = DeviceUtil.getDeviceInfo(client.getDevices());
            if(deviceInfo!=null && !StringUtils.isEmpty(deviceInfo.getPushId())){
                pushNotificationService.sendPushMessageToDevice(resolveDeviceType(client), deviceInfo.getPushId(), "К сожалению, курьер не найден.");
            }
        } catch(App42Exception exception){
            //exception.printStackTrace();
            LOGGER.info("exception in courierOrderCanceledToClientAndCourier: " + exception.getMessage());
        }
    }



    @Override
    public void courierSendLocationToClient(Order order, DriverLocation location) {

    }


    @Override
    public void courierOrderFired(Order order, int countNotified) {

    }



    @Override
    public void cancelOrder(Order order) {
        try {
            Client client = order.getClient();
            DeviceInfo deviceInfo = DeviceUtil.getDeviceInfo(client.getDevices());
            if(deviceInfo!=null && !StringUtils.isEmpty(deviceInfo.getPushId())){
                pushNotificationService.sendPushMessageToDevice(resolveDeviceType(client), deviceInfo.getPushId(), "Ваш заказ отменен.");
            }
        } catch(App42Exception exception){
            LOGGER.info("exception in cancelOrder: " + exception.getMessage());
        }
    }


    @Override
    public void driverLate() {

    }



    @Override
    public void courierOrderConfirm(Order order) {
        try {
            Client client = order.getClient();
            DeviceInfo deviceInfo = DeviceUtil.getDeviceInfo(client.getDevices());
            if(deviceInfo!=null && !StringUtils.isEmpty(deviceInfo.getPushId())){
                pushNotificationService.sendPushMessageToDevice(resolveDeviceType(client), deviceInfo.getPushId(), "Ваш заказ принят. Чтобы мы начали выполнять ваш заказ, подтвердите его в приложении Таксисто в разделе Zavezu «Текущие заказы».");
            }
        } catch(App42Exception exception){
            LOGGER.info("exception in courierOrderConfirm: " + exception.getMessage());
            //exception.printStackTrace();
        }
    }

    @Override
    public void courierNewOrderDispatcher(Order order) {

    }

    @Override
    public void findCouriers(Order order, int count) {

    }

    @Override
    public void courierLate(Order order, int time) {

    }



    @Override
    public void courierAllPurchased(Order order) {
        try {
            Client client = order.getClient();
            DeviceInfo deviceInfo = DeviceUtil.getDeviceInfo(client.getDevices());
            if(deviceInfo!=null && !StringUtils.isEmpty(deviceInfo.getPushId())){
                pushNotificationService.sendPushMessageToDevice(resolveDeviceType(client), deviceInfo.getPushId(), "Курьер едет к вам");
            }
        } catch(App42Exception exception){
            //exception.printStackTrace();
            LOGGER.info("courierAllPurchased exception: " + exception.getMessage());
        }
    }



    @Override
    public void courierOrderAssign(Order order, int arrivalTime, DriverLocation location) {
        try {
            Client client = order.getClient();
            DeviceInfo deviceInfo = DeviceUtil.getDeviceInfo(client.getDevices());
            if(deviceInfo!=null && !StringUtils.isEmpty(deviceInfo.getPushId())){
                pushNotificationService.sendPushMessageToDevice(resolveDeviceType(client), deviceInfo.getPushId(), "На Ваш заказ назначен курьер.");
            }
        } catch(App42Exception exception){
            LOGGER.info("exception in courierOrderAssign: " + exception.getMessage());
            //exception.printStackTrace();
        }
    }


    @Override
    public void serviceSuccessfullyActivated(long clientId, String message) {
        try {
            Client client = clientRepository.findOne(clientId);
            DeviceInfo deviceInfo = DeviceUtil.getDeviceInfo(client.getDevices());
            if(deviceInfo!=null && !StringUtils.isEmpty(deviceInfo.getPushId())){
                if(!StringUtils.isEmpty(message)){
                    pushNotificationService.sendPushMessageToDevice(resolveDeviceType(client), deviceInfo.getPushId(), message);
                }
            } else {
                LOGGER.info(String.format("Не могу отправить пуш, deviceInfo = null или пустой PushId, клиент %s", client.getId()));
            }
        } catch(App42Exception exception){
            LOGGER.info("serviceSuccessfullyActivated exception: " + exception.getMessage());
            //throw App42HelperException.wrapException(exception);
        }
    }

    /* - courier - */




    @Override
    public void transferMissionToDriver(long missionId, long from_driverId, long to_driverId, String state, boolean booked) {
    }

    @Override
    public void driverRefresh(long driverId, long webUserId) {
    }

    @Override
    public void autosearchCanceled(long missionId) {
    }

    @Override
    public void driverAssigneSecondOrder(Mission mission, long driverId, int arrivalTime, DriverLocation location) {
    }

    @Override
    public void driverLocation(Mission mission, DriverLocation driverLocation) {

    }

    private String resolveDeviceType(Client client){
        String deviceType = "android_client";
        if(!StringUtils.isEmpty(client.getDeviceType())){
            if(client.getDeviceType().equals("APPLE")){
                 deviceType = "iphone";
            }
        }
           return deviceType;
    }



    @Override
    public void missionAssigneMsg(Mission mission, long driverId, int arrivalTime, DriverLocation location, String message) {
        try {
            Client client = mission.getClientInfo();
            DeviceInfo deviceInfo = DeviceUtil.getDeviceInfo(client.getDevices());
            if(deviceInfo!=null && !StringUtils.isEmpty(deviceInfo.getPushId())){
                if(!StringUtils.isEmpty(message)){
                    pushNotificationService.sendPushMessageToDevice(resolveDeviceType(client), deviceInfo.getPushId(), message);
                }
            } else {
                    LOGGER.info(String.format("Не могу отправить пуш, deviceInfo = null или пустой PushId, клиент %s", client.getId()));
            }
        } catch(App42Exception exception){
            LOGGER.info("missionAssigneMsg exception: " + exception.getMessage());
        }
    }


    @Override
    public void openMissionCard(long missionId) {

    }

    @Override
    public void bookedMissionFired(long missionId, String time) {

    }

    @Override
    public void bookedMissionFailed(long missionId) {

    }

    @Override
    public void clientCustomMessage(long clientId, String message) {
        if(!StringUtils.isEmpty(message)){
            Client client = clientRepository.findOne(clientId);
            try {
                if (client != null) {
                    DeviceInfo deviceInfo = DeviceUtil.getDeviceInfo(client.getDevices());
                    if(deviceInfo!=null && !StringUtils.isEmpty(deviceInfo.getPushId())){
                        pushNotificationService.sendPushMessageToDevice(resolveDeviceType(client), deviceInfo.getPushId(), message);
                    }
                }
            } catch(App42Exception exception){
                LOGGER.info("exception in clientCustomMessage: " + exception.getMessage());
            }
        }
    }




    @Override
    public void missionWaitPaymentStart(long clientId, String message) {
       Client client = clientRepository.findOne(clientId);
        try {
            if (client != null) {
                DeviceInfo deviceInfo = DeviceUtil.getDeviceInfo(client.getDevices());
                if(deviceInfo!=null && !StringUtils.isEmpty(deviceInfo.getPushId())){
                    if(!StringUtils.isEmpty(message)){
                        pushNotificationService.sendPushMessageToDevice(resolveDeviceType(client), deviceInfo.getPushId(), message);
                    }
                } else{
                    LOGGER.info(String.format("Не могу отправить пуш, deviceInfo = null или пустой PushId, клиент %s", clientId));
                }
            }
        } catch(App42Exception exception){
            LOGGER.info("missionWaitPaymentStart exception: " + exception.getMessage());
            //throw App42HelperException.wrapException(exception);
        }
    }


    @Override
    public void driverChange(Mission mission, String message) {
        try {
            Client client = mission.getClientInfo();
            DeviceInfo deviceInfo = DeviceUtil.getDeviceInfo(client.getDevices());
            if(deviceInfo!=null && !StringUtils.isEmpty(deviceInfo.getPushId())){
                //String message = commonService.getPropertyValue("driver_change_push_mess");
                if(!StringUtils.isEmpty(message)){
                    pushNotificationService.sendPushMessageToDevice(resolveDeviceType(client), deviceInfo.getPushId(), message);
                }
            } else{
                LOGGER.info(String.format("Не могу отправить пуш, deviceInfo = null или пустой PushId, клиент %s", client.getId()));
            }
        } catch(App42Exception exception){
            LOGGER.info("driverChange exception: " + exception.getMessage());
            //throw App42HelperException.wrapException(exception);
        }
    }



    @Override
    public void missionDriverCanceled(Mission mission, String message) {
        try {
            Client client = mission.getClientInfo();
            DeviceInfo deviceInfo = DeviceUtil.getDeviceInfo(client.getDevices());
            if(deviceInfo!=null && !StringUtils.isEmpty(deviceInfo.getPushId())){
                //String message = commonService.getPropertyValue("driver_canceled_mission_push_mess");
                if(!StringUtils.isEmpty(message)){
                    pushNotificationService.sendPushMessageToDevice(resolveDeviceType(client), deviceInfo.getPushId(), message);
                }
            } else{
                LOGGER.info(String.format("Не могу отправить пуш, deviceInfo = null или пустой PushId, клиент %s", client.getId()));
            }
        } catch(App42Exception exception){
            LOGGER.info("missionDriverCanceled exception: " + exception.getMessage());
            //throw App42HelperException.wrapException(exception);
        }
    }


    @Override
    public void driverArrived(Mission mission, boolean booked, String message) {
        try {
            Client client = mission.getClientInfo();
            DeviceInfo deviceInfo = DeviceUtil.getDeviceInfo(client.getDevices());
            if(deviceInfo!=null && !StringUtils.isEmpty(deviceInfo.getPushId())){
                if(!StringUtils.isEmpty(message)){
                    pushNotificationService.sendPushMessageToDevice(resolveDeviceType(client), deviceInfo.getPushId(), message);
                }
            } else{
                LOGGER.info(String.format("Не могу отправить пуш, deviceInfo = null или пустой PushId, клиент %s", client.getId()));
            }
        } catch(App42Exception exception){
            LOGGER.info("driverArrived exception: " + exception.getMessage());
            //throw App42HelperException.wrapException(exception);
        }
    }



    @Override
    public void driverCustomMessage(long driverId, String message) {

    }

    @Override
    public void driverCustomMessageARM(long driverId, String text) {

    }

    @Override
    public void regionChange(long driverId, String message, boolean fromRemote) {

    }

    @Override
    public void paymentCardAnswer(long missionId, boolean answer) {

    }

    @Override
    public void askClientForCardPayment(long missionId, int sum) {

    }

    @Override
    public void missionBecameUnavailable(long missionId, long driverId) {

    }

    @Override
    public void missionCancelSecondOrder(long missionId, long driverId) {

    }


    @Override
    public void findDrivers(Mission mission, int count, float radius) {

    }


    @Override
    public void askClientForAutoSearch(Mission mission) {

    }

    private static class App42HelperException{
        private static CustomException wrapException(App42Exception exc){
            CustomException customException = new CustomException();
            customException.setCode(exc.getAppErrorCode());
            customException.setMessage(exc.getMessage());
                return customException;
        }
    }

}












/*
switch (exc.getAppErrorCode()){
                case 1400:{
                    customException.setCode(1400);
                    customException.setMessage("BAD REQUEST - The Request parameters are invalid.");
                    break;
                }
                case 1401:{
                    customException.setCode(1401);
                    customException.setMessage("UNAUTHORIZED - Client is not authorized.");
                    break;
                }
                case 1500:{
                    customException.setCode(1500);
                    customException.setMessage("INTERNAL SERVER ERROR - Internal Server Error. Please try again.");
                    break;
                }
                case 1700:{
                    customException.setCode(1700);
                    customException.setMessage("BAD REQUEST - User by the name '@userName' already registered with the device '@deviceToken'.");
                    break;
                }
                case 1701:{
                    customException.setCode(1701);
                    customException.setMessage("BAD REQUEST - Channel by the name '@channelName' already exist");
                    break;
                }
                case 1702:{
                    customException.setCode(1702);
                    customException.setMessage("NOT FOUND - Channel by the name '@channelName' does not exist.");
                    break;
                }
                case 1703:{
                    customException.setCode(1703);
                    customException.setMessage("NOT FOUND - User by the name '@userName' does not have any device registered");
                    break;
                }
                case 1704:{
                    customException.setCode(1704);
                    customException.setMessage("BAD REQUEST - User by the name '@userName' already subscribed for the channel '@channelName'");
                    break;
                }
                case 1705:{
                    customException.setCode(1705);
                    customException.setMessage("NOT FOUND - User by the name '@userName' not subscribed for any channel");
                    break;
                }
                case 1706:{
                    customException.setCode(1706);
                    customException.setMessage("NOT FOUND - No device is registered with the App");
                    break;
                }
                case 1707:{
                    customException.setCode(1707);
                    customException.setMessage("NOT FOUND - No device is Subscribed to the channel '@channelName'.");
                    break;
                }
                case 1708:{
                    customException.setCode(1708);
                    customException.setMessage("NOT FOUND - Channel by the name '@channelName' device '@deviceToken' does not subscribe.");
                    break;
                }
                case 1709:{
                    customException.setCode(1709);
                    customException.setMessage("NOT FOUND - User by the name '@userName' device token '@deviceToken' does not registered.");
                    break;
                }
                case 1710:{
                    customException.setCode(1710);
                    customException.setMessage("NOT FOUND - No device registered for user list '@userList'");
                    break;
                }
                case 1711:{
                    customException.setCode(1711);
                    customException.setMessage("NOT FOUND - No target user found for given query");
                    break;
                }
                case 1712:{
                    customException.setCode(1712);
                    customException.setMessage("NOT FOUND - In Active Users with date rnage '@startDate'and '@endDate' does not exist.");
                    break;
                }
                case 1713:{
                    customException.setCode(1713);
                    customException.setMessage("BAD REQUEST - User by the name '@userName' already unsubscribe with the device '@deviceToken'");
                    break;
                }
                case 1714:{
                    customException.setCode(1714);
                    customException.setMessage("BAD REQUEST - User by the name '@userName' already subscribe with the device '@deviceToken'");
                    break;
                }
                case 1715:{
                    customException.setCode(1715);
                    customException.setMessage("BAD REQUEST - Your previous request already in process.Please Try after sometime.");
                    break;
                }
                default: {
                    customException.setCode(exc.getAppErrorCode());
                    customException.setMessage("App42PushException: "+exc.getMessage());
                }
 */