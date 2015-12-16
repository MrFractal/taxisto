package ru.trendtech.services.push;

/**
 * Created by ivanenok on 4/4/2014.
 */

import com.google.android.gcm.server.*;
import com.google.android.gcm.server.Message.Builder;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.trendtech.domain.DeviceInfo;
import ru.trendtech.integration.push.PushNotificationException;
import ru.trendtech.utils.StrUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class AndroidPushManager extends PushManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(AndroidPushManager.class);

    private Sender sender;

    private static List<String> buildDevicesList(DeviceInfo[] devices) {
        ArrayList<String> result = new ArrayList<>();
        if (devices != null) {
            result.addAll(Collections2.transform(DevicesUtils.findValidAndroidDevices(Arrays.asList(devices)), new ToAndroidDevices()));
        }
        return result;
    }

    /**
     * configure android GSM account for the sending push notification to the
     * android device.
     */
    @Override
    public boolean configureAccount(String apiKey, String password, boolean production) {
        sender = new Sender(apiKey);
        return true;
    }

    /**
     * this method send the notification to the device and return the status of
     * the notification.
     */
    @Override
    public void sendMessage(Object message, Map<String, String> param, DeviceInfo... devices) throws PushNotificationException {
        if (sender != null) {
            try {
                String msg = convertMassage(message);
                Builder builder = new Message.Builder()
                        .addData("data.payload", msg)
                        .collapseKey(StrUtils.generateAlphaNumString(9));
                for (String key : param.keySet()) {
                    builder.addData(key, param.get(key));
                }
                Message gcmMessage = builder.build();
                List<String> devicesList = buildDevicesList(devices);
                MulticastResult multicastResult = sender.send(gcmMessage, devicesList, 5);
                for (Result result : multicastResult.getResults()) {
                    LOGGER.debug("send result: ", result);
                    if (result != null) {
                        if (result.getMessageId() != null) {
                            String canonicalRegId = result
                                    .getCanonicalRegistrationId();
                            if (canonicalRegId != null) {
                                LOGGER.info("Canonical Registration: " + canonicalRegId);
                            }
                        } else {
                            String error = result.getErrorCodeName();
                            if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                                LOGGER.info("Error Code Name: " + result.getErrorCodeName());
                            }
                        }
                    }
                }
            } catch (IOException e) {
                LOGGER.info(e.toString());
            }
        } else {
            throw new PushNotificationException("Sender not configured properly");
        }
    }

    private static class ToAndroidDevices implements Function<DeviceInfo, String> {
        @Override
        public String apply(DeviceInfo deviceInfo) {
            return deviceInfo.getToken();
        }
    }
}