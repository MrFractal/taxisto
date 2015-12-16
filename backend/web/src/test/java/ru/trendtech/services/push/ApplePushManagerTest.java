package ru.trendtech.services.push;

//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import javapns.Push;
//import javapns.notification.PushedNotifications;
//import org.joda.time.DateTime;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.testng.annotations.Test;
//import ru.trendtech.common.mobileexchange.model.common.push.PushMessage;
//import ru.trendtech.common.mobileexchange.model.notifications.DriverArrivedNotification;
//import ru.trendtech.integration.push.devices.AppleDevice;
//import ru.trendtech.integration.push.devices.MobileDevice;
//import ru.trendtech.integration.push.manager.ApplePushManager;
//import ru.trendtech.integration.push.messages.AppleMessage;
//import ru.trendtech.utils.SecurityUtils;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * Created by ivanenok on 4/4/2014.
// */
//public class ApplePushManagerTest {
//    private static final Logger LOGGER = LoggerFactory.getLogger(ApplePushManagerTest.class);
//
//    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
//
//    private static final String CONF_STRING = "certs/CertificatesAPNs.p12";
//
//    private static final String PASSWORD = "12345678";
//
//    private static final String TOKEN = "beabd16f97e0450b12a431a29a8f80c165a3be79dd7e55d3e0679436859bc987";
//    private static final String TOKEN_MAX_IPAD = "30884f9f0fb6010a8090c96cc866315a68b339fd51bba545da4a80b69e9fa867";
//
//    @Test
//    public void testConfigureAccount() throws Exception {
//        ApplePushManager manager = new ApplePushManager();
//        boolean configured = manager.configureAccount(CONF_STRING, PASSWORD, false);
//    }
//
//    @Test
//    public void testSentMessage() throws Exception {
//        ApplePushManager manager = new ApplePushManager();
//        boolean res = manager.configureAccount(CONF_STRING, PASSWORD, false);
//        if (res) {
//            HashMap<String, String> map = new HashMap<>();
//            map.put("key1", "value1");
//            map.put("key2", "value2");
//            manager.sendMessage(new DriverArrivedNotification(), map, new AppleDevice(TOKEN));
//        }
//    }
//
//    @Test
//    public void testSentMessageViaManager() throws Exception {
//        ApplePushManager pushManager = new ApplePushManager();
//        boolean configured = pushManager.configureAccount(CONF_STRING, PASSWORD, false);
//        if (configured) {
//            String asString = getDriverArrivedMessage();
//            AppleMessage notification =
//                    AppleMessage.complex()
//                            .alert("alert message. Class: Notification class! " + TOKEN)
//                            .sound("default")
//                            .message(asString);
//            PushedNotifications notifications = pushManager.sendMessage(notification, buildDevices());
//            LOGGER.debug("Notification serponse! " + notification);
//        }
//    }
//
//    private List<MobileDevice> buildDevices() {
//        MobileDevice mobileDevice1 = new AppleDevice(TOKEN, DateTime.now().getMillis());
//        MobileDevice mobileDevice2 = new AppleDevice(TOKEN_MAX_IPAD, DateTime.now().getMillis());
//
//        return Arrays.asList(mobileDevice1, mobileDevice2);
//    }
//
//    @Test
//    public void testSentMessage1() throws Exception {
//        String asString = getDriverArrivedMessage();
//
////        PushNotificationPayload notificationPayload = new PushNotificationPayload(asString);
//
//        AppleMessage appleMessage = AppleMessage.complex().alert("alert messagae").sound("default").message(asString);
//
//        PushedNotifications payload = Push.payload(appleMessage, SecurityUtils.getCertificateStream(CONF_STRING), PASSWORD, false, new String[]{TOKEN});
////
////        AppleMessage.complex().contentAvailable().alert("test alert!!").addCustomDictionary("data", "json string");
////        PushedNotifications test1 = Push.contentAvailable(SecurityUtils.getCertificateStream(CONF_STRING), PASSWORD, false, new String[]{TOKEN});
////        Push.combined(SecurityUtils.getCertificateStream(CONF_STRING), PASSWORD, false, new String[]{TOKEN});
//    }
//
//    private String getDriverArrivedMessage() throws JsonProcessingException {
//        PushMessage message = new PushMessage();
//        message.setT(1);
//        DriverArrivedNotification driverArrivedNotification = new DriverArrivedNotification();
//        driverArrivedNotification.setId(1922);
//        driverArrivedNotification.setFreeTime(15);
//        message.setO(OBJECT_MAPPER.writeValueAsString(driverArrivedNotification));
//
//        return OBJECT_MAPPER.writeValueAsString(message);
//    }
//}
