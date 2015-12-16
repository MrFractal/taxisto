package ru.trendtech.integration.push.manager;

/**
 * Created by ivanenok on 4/4/2014.
 */

import com.google.android.gcm.server.*;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.trendtech.integration.push.PushNotificationException;
import ru.trendtech.integration.push.devices.DevicesUtils;
import ru.trendtech.integration.push.devices.MobileDevice;

//import javax.mail.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AndroidPushManager extends PushManager<MulticastResult> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AndroidPushManager.class);

    private Sender sender;

    private static List<String> buildDevicesList(List<MobileDevice> devices) {
        ArrayList<String> result = new ArrayList<>();
        if (devices != null) {
            result.addAll(Collections2.transform(DevicesUtils.findValidAndroidDevices(devices), new ToAndroidDevices()));
        }
        return result;
    }

    /**
     * configure android GSM account for the sending push notification to the
     * android device.
     */
    @Override
    public boolean configureAccount(String configuration, String password, boolean production) {
        sender = new Sender(configuration);
        return true;
    }

    /**
     * this method send the notification to the device and return the status of
     * the notification.
     */
    @Override
    public MulticastResult sendMessage(Object message, Map<String, String> param, List<MobileDevice> devices) throws PushNotificationException {
        MulticastResult result = null;
        if (sender != null) {
            try {
                Message msg = buildMessage(message, param);
                LOGGER.debug("Notification prepared for sending to clients: " + msg.toString());
                List<String> devicesList = buildDevicesList(devices);
                if (!devicesList.isEmpty()) {
                    result = sender.send(msg, devicesList, 5);
                    List<Result> results = result.getResults();
                    for (int i = 0; i < results.size(); i++) {
                        Result item = results.get(i);
                        String deviceId = devicesList.get(i);
                        if (item != null) {
                            if (item.getMessageId() != null) {
                                String additionalMsg = (item.getCanonicalRegistrationId() != null) ? "Canonical ID: " + item.getCanonicalRegistrationId() : "";
                                LOGGER.info("Notification with ID {} sent to user. User device ID {}. ", item.getMessageId(), deviceId, additionalMsg);
                            } else {
                                String errorCodeName = item.getErrorCodeName();
                                LOGGER.error("ERROR! Notification NOT sent to user. User device ID {}. Error code: {}.", deviceId, errorCodeName);
                            }
                        } else {
                            LOGGER.error("Pesult item can't be NULL! for device id []", deviceId);
                        }
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Problem on sending notification to android device", e);
            }
        } else {
            throw new PushNotificationException("Sender not configured properly");
        }
        return result;
    }




    private Message buildMessage(Object message, Map<String, String> param) throws PushNotificationException {
        Message.Builder builder;
        Preconditions.checkNotNull(message);
        if (message instanceof Message.Builder) {
            builder = (Message.Builder) message;
        } else {
            throw new PushNotificationException("Can't restore push message from string: + " + message);
        }
        if ((param != null) && (!(param.isEmpty()))) {
            for (Map.Entry<String, String> entry : param.entrySet()) {
                builder.addData(entry.getKey(), param.get(entry.getValue()));
            }
        }
        return builder.build();
    }

    private static class ToAndroidDevices implements Function<MobileDevice, String> {
        @Override
        public String apply(MobileDevice deviceInfo) {
            return deviceInfo.getToken();
        }
    }
}