package ru.trendtech.services.notifications;

//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.android.gcm.server.Message;
//import com.google.common.base.Function;
//import com.google.common.base.Predicate;
//import com.google.common.collect.Collections2;
//import net.iharder.Base64;
//import org.json.JSONException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//import ru.trendtech.common.mobileexchange.model.common.ItemLocation;
//import ru.trendtech.common.mobileexchange.model.common.push.ActionTypes;
//import ru.trendtech.common.mobileexchange.model.common.push.PushMessage;
//import ru.trendtech.common.mobileexchange.model.notifications.*;
//import ru.trendtech.domain.Client;
//import ru.trendtech.domain.DeviceInfo;
//import ru.trendtech.domain.Driver;
//import ru.trendtech.domain.Mission;
//import ru.trendtech.integration.push.PushNotificationException;
//import ru.trendtech.integration.push.devices.AndroidDevice;
//import ru.trendtech.integration.push.devices.AppleDevice;
//import ru.trendtech.integration.push.devices.MobileDevice;
//import ru.trendtech.integration.push.manager.PushManager;
//import ru.trendtech.integration.push.messages.AppleMessage;
//import ru.trendtech.models.ModelsUtils;
//import ru.trendtech.repositories.DriverRepository;
//import ru.trendtech.repositories.MissionRepository;
//import ru.trendtech.services.push.PushManagerFactory;
//import ru.trendtech.services.push.PushServiceKind;
//import javax.annotation.Nullable;
//import java.io.UnsupportedEncodingException;
//import java.util.*;

/**
* Created by ivanenok on 4/14/2014.
*/

//
//@Component
//@Transactional
//public class NotificationsServiceImpl implements NotificationsService {
//    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationsServiceImpl.class);
//
//    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
//
//    @Value("${sms.active}")
//    private final boolean useSMSGate = false;
//
//    @Autowired
//    private DriverRepository driverRepository;
//
//    @Autowired
//    private MissionRepository missionRepository;
//
//    @Autowired
//    private PushManagerFactory pushManagerFactory;
//
//    @Override
//    @Transactional
//    public void newMissionAvailable(long missionId) {
//        Mission mission = missionRepository.findOne(missionId);
//        if (mission != null) {
//            ArrayList<DeviceInfo> devices = findAllActiveDriverDevices();
//            DriverRequiredNotification notification = new DriverRequiredNotification();
//            notification.setMissionInfo(ModelsUtils.toModel(mission));
//            sendNotification(ActionTypes.DRIVER_REQUIRED, notification, devices);
//        }
//    }
//
//    private PushMessage buildPushMessage(int action, Object notification) {
//        PushMessage pushMessage = new PushMessage();
//        pushMessage.setT(action);
//        try {
//            String valueAsString = OBJECT_MAPPER.writeValueAsString(notification);
//            byte[] bytes = valueAsString.getBytes("UTF-8");
//            String encodedString = Base64.encodeBytes(bytes);
//            pushMessage.setO(encodedString);
//        } catch (JsonProcessingException | UnsupportedEncodingException e) {
//            LOGGER.error("Problem on notification serialization!", e);
//        }
//        return pushMessage;
//    }
//
//    private void sendNotification(int action, Object notification, ArrayList<DeviceInfo> devices) {
//        try {
//            List<MobileDevice> devicesInfo = new ArrayList<>(Collections2.transform(devices, new Function<DeviceInfo, MobileDevice>() {
//                @Nullable
//                @Override
//                public MobileDevice apply(DeviceInfo input) {
//                    MobileDevice result;
//                    Objects.requireNonNull(input);
//                    if (DeviceInfo.Type.APPLE.equals(input.getType())) {
//                        result = new AppleDevice(input.getToken(), input.getLastUpdate().getMillis());
//                    } else if (DeviceInfo.Type.ANDROID.equals(input.getType())) {
//                        result = new AndroidDevice(input.getToken(), input.getLastUpdate().getMillis());
//                    } else {
//                        throw new IllegalArgumentException("Unknown device type!");
//                    }
//                    return result;
//                }
//            }));
//
//            AppleMessage appleMessage = buildAppleNotification(action, notification);
//            Message.Builder googleMassageBuilder = buildGoogleNotification(action, notification);
//
//            PushManager applePushManager = pushManagerFactory.getInstance(PushServiceKind.APPLE);
//            PushManager androidPushManager = pushManagerFactory.getInstance(PushServiceKind.GOOGLE);
//
//            Map<String, String> emptyMap = Collections.emptyMap();
//
//            sendNotification(androidPushManager, googleMassageBuilder, emptyMap, devicesInfo);
//            sendNotification(applePushManager, appleMessage, emptyMap, devicesInfo);
//        } catch (JsonProcessingException | JSONException e) {
//            LOGGER.error("Problem on posting notification in JSON", e);
//        }
//    }
//
//    private AppleMessage buildAppleNotification(int action, Object notification) throws JsonProcessingException, JSONException {
//        PushMessage message = buildPushMessage(action, notification);
//        return AppleMessage.complex()
//                .alert("Taxisto notification. Type: " + notification.getClass().getSimpleName())
//                .sound("default")
//                .message(OBJECT_MAPPER.writeValueAsString(message));
//    }
//
//    private Message.Builder buildGoogleNotification(int action, Object notification) throws JsonProcessingException {
//        PushMessage message = buildPushMessage(action, notification);
//        return new Message.Builder()
//                .delayWhileIdle(true)
//                .timeToLive(5)
//                .collapseKey("test")
//                .addData("data", OBJECT_MAPPER.writeValueAsString(message));
//    }
//
//    private void sendNotification(PushManager pushManager, Object message, Map<String, String> map, List<MobileDevice> devices) {
//        try {
//            pushManager.sendMessage(message, map, devices);
//        } catch (PushNotificationException e) {
//            LOGGER.error("Problem on sending to push manager.", e);
//        }
//    }
//
//    private ArrayList<DeviceInfo> findActiveDevices(Collection<DeviceInfo> deviceInfos) {
//        return new ArrayList<>(Collections2.filter(deviceInfos, new Predicate<DeviceInfo>() {
//            @Override
//            public boolean apply(DeviceInfo deviceInfo) {
//                return DeviceInfo.State.ACTIVE.equals(deviceInfo.getState());
//            }
//        }));
//    }
//
//    private ArrayList<DeviceInfo> findActiveDevices(Client client) {
//        ArrayList<DeviceInfo> result = new ArrayList<>();
//        if (client != null) {
//            result.addAll(findActiveDevices(client.getDevices()));
//        }
//        return result;
//    }
//
//    private ArrayList<DeviceInfo> findActiveDevices(Driver driver) {
//        ArrayList<DeviceInfo> result = new ArrayList<>();
//        if (driver != null) {
//            result.addAll(findActiveDevices(driver.getDevices()));
//        }
//        return result;
//    }
//
//    private ArrayList<DeviceInfo> findActiveClientDevices(Mission mission) {
//        return findActiveDevices(mission.getClientInfo());
//    }
//
//    private ArrayList<DeviceInfo> findActiveDriverDevices(Mission mission) {
//        return findActiveDevices(mission.getDriverInfo());
//    }
//
//    private ArrayList<DeviceInfo> findAllActiveDriverDevices() {
//        ArrayList<DeviceInfo> devices = new ArrayList<>();
//        for (Driver driver : driverRepository.findByState(Driver.State.AVAILABLE)) {
//            devices.addAll(findActiveDevices(driver.getDevices()));
//        }
//        return devices;
//    }
//
//    @Override
//
//    public void driverLate(long driverId, int lateTime) {
//        Driver driver = driverRepository.findOne(driverId);
//        if (driver != null) {
//            Mission mission = driver.getCurrentMission();
//            ArrayList<DeviceInfo> devices = findActiveClientDevices(mission);
//            DriverLateNotification notification = new DriverLateNotification();
//            notification.setId(driverId);
//            notification.setTime(lateTime);
//            sendNotification(ActionTypes.DRIVER_LATE, notification, devices);
//        }
//    }
//
//    @Override
//    public void driverAssigned(long driverId, long missionId, int arrivalTime, boolean booked) {
//        Driver driver = driverRepository.findOne(driverId);
//        if (driver != null) {
//            Mission mission = missionRepository.findOne(missionId);
//            if (mission != null){
//                ArrayList<DeviceInfo> devices = findActiveClientDevices(mission);
//                AssignedDriverNotification notification = new AssignedDriverNotification();
//                notification.setId(driverId);
//                notification.setTime(arrivalTime);
//                notification.setBooked(booked);
//                sendNotification(ActionTypes.DRIVER_ASSIGNED, notification, devices);
//            }
//        }
//    }
//
//    @Override
//    public void cancelMissionByClient(long missionId) {
//        Mission mission = missionRepository.findOne(missionId);
//        if (mission != null) {
//            cancelMission(mission.getId(), findActiveDriverDevices(mission));
//        }
//    }
//
//    @Override
//    public void cancelMissionByDriver(long missionId) {
//        Mission mission = missionRepository.findOne(missionId);
//        if (mission != null) {
//            cancelMission(mission.getId(), findActiveClientDevices(mission));
//        }
//    }
//
//    private void cancelMission(long missionId, ArrayList<DeviceInfo> devices) {
//        TripCanceledNotification notification = new TripCanceledNotification();
//        notification.setMissionId(missionId);
//        sendNotification(ActionTypes.TRIP_CANCELED, notification, devices);
//    }
//
//    @Override
//    public void driverChangedLocation(long driverId, ItemLocation location) {
//        Driver driver = driverRepository.findOne(driverId);
//        if (driver != null) {
//            Mission mission = driver.getCurrentMission();
//            ArrayList<DeviceInfo> devices = findActiveClientDevices(mission);
//            AssignedDriverLocationNotification notification = new AssignedDriverLocationNotification();
//            notification.setId(driverId);
//            notification.setLoc(location);
//            sendNotification(ActionTypes.LOCATION_DRIVER_ASSIGNED, notification, devices);
//        }
//    }
//
//    @Override
//    public void driverArrived(long driverId, int freeTime) {
//        Driver driver = driverRepository.findOne(driverId);
//        if (driver != null) {
//            Mission mission = driver.getCurrentMission();
//            ArrayList<DeviceInfo> devices = findActiveClientDevices(mission);
//            DriverArrivedNotification notification = new DriverArrivedNotification();
//            notification.setId(driverId);
//            notification.setFreeTime(freeTime);
//            sendNotification(ActionTypes.DRIVER_ARRIVED, notification, devices);
//        }
//    }
//
//    private void notifyMission(long missionId, boolean begin) {
//        Mission mission = missionRepository.findOne(missionId);
//        if (mission != null) {
//            ArrayList<DeviceInfo> devices = findActiveClientDevices(mission);
//            TripNotification notification = new TripNotification();
//            notification.setMissionId(missionId);
//            notification.setBegin(begin);
//            sendNotification(begin ? ActionTypes.TRIP_STARTED : ActionTypes.TRIP_ENDED, notification, devices);
//        }
//    }
//
//    private void notifyMissionPause(long missionId, ItemLocation location, boolean begin) {
//        Mission mission = missionRepository.findOne(missionId);
//        if (mission != null) {
//            ArrayList<DeviceInfo> devices = findActiveClientDevices(mission);
//            TripPauseNotification notification = new TripPauseNotification();
//            notification.setMissionId(missionId);
//            notification.setLocation(location);
//            notification.setBegin(begin);
//            sendNotification(begin ? ActionTypes.TRIP_PAUSE_STARTED : ActionTypes.TRIP_PAUSE_ENDED, notification, devices);
//        }
//    }
//
//    @Override
//    public void missionPauseStart(long missionId, ItemLocation location) {
//        notifyMissionPause(missionId, location, true);
//    }
//
//    @Override
//    public void missionPauseEnd(long missionId, ItemLocation location) {
//        notifyMissionPause(missionId, location, false);
//    }
//
//    @Override
//    public void missionStart(long missionId) {
//        notifyMission(missionId, true);
//    }
//
//    @Override
//    public void missionEnd(long missionId) {
//        notifyMission(missionId, false);
//    }
//
//    @Override
//    public void missionPayment(long missionId) {
//        Mission mission = missionRepository.findOne(missionId);
//        if (mission != null) {
//            ArrayList<DeviceInfo> devices = findActiveClientDevices(mission);
//            PaymentNotification notification = new PaymentNotification();
//            notification.setMissionId(missionId);
//            sendNotification(ActionTypes.TRIP_PAYMENT, notification, devices);
//        }
//    }
//
//    @Override
//    public void clientReadyToGo(long missionId, int expectedTime) {
//        Mission mission = missionRepository.findOne(missionId);
//        if (mission != null) {
//            ArrayList<DeviceInfo> devices = findActiveDriverDevices(mission);
//            ReadyToGoNotification notification = new ReadyToGoNotification();
//            notification.setId(mission.getClientInfo().getId());
//            notification.setTime(expectedTime);
//            sendNotification(ActionTypes.CLIENT_READY_TO_GO, notification, devices);
//        }
//    }
//
//    @Override
//    public void availableWiFi(long missionId, String networkId, String password) {
//        Mission mission = missionRepository.findOne(missionId);
//        if (mission != null) {
//            ArrayList<DeviceInfo> devices = findActiveClientDevices(mission);
//            WifiAvailableNotification notification = new WifiAvailableNotification();
//            notification.setMissionId(missionId);
//            notification.setNetworkId(networkId);
//            notification.setPassword(password);
//            sendNotification(ActionTypes.WIFI_AVAILABLE, notification, devices);
//        }
//    }
//
//    @Override
//    public void bookedMissionConfirm(Long missionId) {
//        Mission mission = missionRepository.findOne(missionId);
//        if (mission != null) {
//            ArrayList<DeviceInfo> devices = findActiveDriverDevices(mission);
//            BookedMissionConfirmNotification notification = new BookedMissionConfirmNotification();
//            notification.setMissionInfo(ModelsUtils.toModel(mission));
//            sendNotification(ActionTypes.BOOKED_MISSION_CONFIRM, notification, devices);
//        }
//    }
//}
