package ru.trendtech.services.push;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gcm.server.Message;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import ru.trendtech.common.mobileexchange.model.common.push.ActionTypes;
import ru.trendtech.common.mobileexchange.model.common.push.PushMessage;
import ru.trendtech.common.mobileexchange.model.notifications.DriverRequiredNotification;
import ru.trendtech.integration.push.PushNotificationException;
import ru.trendtech.integration.push.devices.AndroidDevice;
import ru.trendtech.integration.push.devices.MobileDevice;
import ru.trendtech.integration.push.manager.AndroidPushManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 * Created by ivanenok on 4/4/2014.
 */
public class AndroidPushManagerTest {


    String a1 = "APA91bHz7YEQn06FlUBCX0kcy7T-8x3VI1L1LUfAC3YFXsOOsrv9hi4XA9R6WysXnJFGcNJUWZNawHRTAuuk1Scarkw_wZJq2tOkIi5qHWErm56cz_yZS0R6I1G8T8NidMObHz4GJoXVQbY9aEJk-IxT93KDNm84pw";
    String a2 = "APA91bFPMBg_UDg3fvy-JiO6W0zVd-uOV_9Z7LVo2rUqEfnB2x6MUfgQJ10kTMwFfJ7D6w3whZmd4mD-QGm12mrSlUS8I6LW44iOv8LIPY0aS-0UnsgIFWeg603C_1Kyd2nwgKwzbLXkiSNiIC5jJU4M5TF_QulIoQ";
    String a3 = "APA91bHuRhy6V2U83uSPx7egkn4t7MzGjfKiRvbW_2-8dWW68VqLf3qnyK7lws5v_KPTgnad2wRnIg1XlnA-fUrRc8ORvxdsifHwF-rxNz3hIG2PHgbun4igNGhDU45NHBjHhEIpX3H_qBqMesw4XLqu1nluB81NOg";
    String a4 = "APA91bFbUTora2n3uEACS--kGbDk8N4nV_-s1jS3-vQXmiVRgsw1dNz5ktDanX9DWUTyqJrMpF0jw72HUmFh2xw4tSGY8LcosqwCXbh_Wgvx7KXmQo1zzmhPkhuVsPdezkZeK4lCVaFBKwrFxbw7QlZZVYJCTx1uHg";

    //    String REQUEST_ID = "APA91bFHUQFl_Pq7htzb8pqgTzZvX4bVo_zciSSGN9YOZfdPO_7sF8M0vF4oKgn79PJja3RGj2vBMQ193VGQjTYUNrwrntuZ9i7w5lm5gK0G1ZDYVDZEcHEkKJqzLpAtlHB4zZDvKB4AsykFz1eCA23cy3Nhjcoaag";
//    private static final String REQUEST_ID = "APA91bFbUTora2n3uEACS--kGbDk8N4nV_-s1jS3-vQXmiVRgsw1dNz5ktDanX9DWUTyqJrMpF0jw72HUmFh2xw4tSGY8LcosqwCXbh_Wgvx7KXmQo1zzmhPkhuVsPdezkZeK4lCVaFBKwrFxbw7QlZZVYJCTx1uHg";
    private static final String REQUEST_ID = "APA91bHz7YEQn06FlUBCX0kcy7T-8x3VI1L1LUfAC3YFXsOOsrv9hi4XA9R6WysXnJFGcNJUWZNawHRTAuuk1Scarkw_wZJq2tOkIi5qHWErm56cz_yZS0R6I1G8T8NidMObHz4GJoXVQbY9aEJk-IxT93KDNm84pw";

    private static final Logger LOGGER = LoggerFactory.getLogger(AndroidPushManagerTest.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final MobileDevice DEVICE_INFO = buildDeviceInfo();
    private final String googleClientsApiKey = "AIzaSyDxprT43JIT5Pr3c9Qy52zoFai85d_GCSA";
    private AndroidPushManager manager = new AndroidPushManager();

    private static MobileDevice buildDeviceInfo() {
        MobileDevice deviceInfo = new AndroidDevice(REQUEST_ID, DateTime.now().getMillis());
        deviceInfo.setToken(REQUEST_ID);
        return deviceInfo;
    }

    @Test
    public void testConfigureAccount() throws Exception {
        boolean googleClientsApiKey1 = manager.configureAccount(googleClientsApiKey, null, false);
        DriverRequiredNotification notification = new DriverRequiredNotification();
        sendMessage(ActionTypes.DRIVER_REQUIRED, notification);
    }

    private void sendMessage(int action, Object notification) throws JsonProcessingException, PushNotificationException {
        Map<String, String> emptyMap = Collections.emptyMap();
        Message.Builder builder = buildGoogleNotification(action, notification);
        manager.sendMessage(builder, emptyMap, Arrays.asList(DEVICE_INFO));
    }

    private PushMessage buildPushMessage(int action, Object notification) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setT(action);
        try {
            pushMessage.setO(OBJECT_MAPPER.writeValueAsString(notification));
        } catch (JsonProcessingException e) {
            LOGGER.error("Problem on notification serialization!", e);
        }
        return pushMessage;
    }

    private Message.Builder buildGoogleNotification(int action, Object notification) throws JsonProcessingException {
        PushMessage message = buildPushMessage(action, notification);
        return new Message.Builder()
                .delayWhileIdle(true)
                .timeToLive(60)
                .collapseKey("test")
                .addData("data", OBJECT_MAPPER.writeValueAsString(message));
    }

    @Test
    public void testSentMessage() throws Exception {
        AndroidPushManager manager = new AndroidPushManager();
        boolean googleClientsApiKey1 = manager.configureAccount(googleClientsApiKey, null, false);
        Message build = new Message.Builder().collapseKey("key")
                .addData("key1", "value1").build();
//        boolean s = manager.sendMessage(REQUEST_ID, build.toString(), "", "", Collections.<String, String>emptyMap());
    }
}
