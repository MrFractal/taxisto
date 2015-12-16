package ru.trendtech.services.push;

/**
 * Created by ivanenok on 4/4/2014.
 */

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import javapns.devices.Device;
import javapns.devices.exceptions.InvalidDeviceTokenFormatException;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.feedback.FeedbackServiceManager;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.NewsstandNotificationPayload;
import javapns.notification.PushNotificationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.trendtech.domain.DeviceInfo;
import ru.trendtech.integration.push.PushNotificationException;
import ru.trendtech.utils.SecurityUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This is the applePushManager class implement the functionality of the push Manager.
 */

@Component
public class ApplePushManager extends PushManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplePushManager.class);

    private PushNotificationManager pushManager = new PushNotificationManager();
    private FeedbackServiceManager feedbackManager = new FeedbackServiceManager();

    public ApplePushManager() {
    }

    private static List<Device> buildDevicesList(DeviceInfo[] devices) {
        ArrayList<Device> result = new ArrayList<>();
        if (devices != null) {
            List<DeviceInfo> validAppleDevices = DevicesUtils.findValidAppleDevices(Arrays.asList(devices));
            result.addAll(Collections2.transform(validAppleDevices, new Function<DeviceInfo, Device>() {
                @Override
                public Device apply(DeviceInfo deviceInfo) {
                    Device device = null;
                    try {
                        device = new BasicDevice(deviceInfo.getToken());
                    } catch (InvalidDeviceTokenFormatException e) {
                        // eat it! we pass only valid devices!
                    }
                    return device;
                }
            }));
        }
        return result;
    }

    /**
     * configure apple account for the sending push notification to the iphone device.
     */
    @Override
    public boolean configureAccount(String confString, String password, boolean production) {
        boolean result = false;
        try {
            AppleNotificationServerBasicImpl serverBasic = new AppleNotificationServerBasicImpl(SecurityUtils.getCertificateStream(confString), password, production);
            pushManager.initializeConnection(serverBasic);
            LOGGER.info("Connection initialized...");
            LOGGER.info("done");
            result = true;
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
        return result;
    }

    /**
     * this method send the notification to the device and return the status of the notification.
     */
    @Override
    public void sendMessage(Object message, Map<String, String> param, DeviceInfo... devices) throws PushNotificationException {
        NewsstandNotificationPayload notification = NewsstandNotificationPayload.contentAvailable();
        try {
            String msg = convertMassage(message);
            notification.addCustomDictionary("data", msg);
            if ((param != null) && (!(param.isEmpty()))) {
                for (String key : param.keySet()) {
                    notification.addCustomDictionary(key, param.get(key));
                }
            }
            List<Device> devicesList = buildDevicesList(devices);
            if (!devicesList.isEmpty()) {
                pushManager.sendNotifications(notification, devicesList);
                pushManager.stopConnection();
            }
        } catch (Exception e) {
            throw new PushNotificationException(e);
        }
    }
}