package ru.trendtech.integration.push.manager;

/**
 * Created by ivanenok on 4/4/2014.
 */

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import javapns.devices.Device;
import javapns.feedback.FeedbackServiceManager;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushedNotifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.trendtech.integration.push.PushNotificationException;
import ru.trendtech.integration.push.devices.DevicesUtils;
import ru.trendtech.integration.push.devices.MobileDevice;
import ru.trendtech.integration.push.messages.AppleMessage;
import ru.trendtech.utils.SecurityUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This is the applePushManager class implement the functionality of the push Manager.
 */

//
//public class ApplePushManager extends PushManager<PushedNotifications> {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(ApplePushManager.class);
//
//    private final PushNotificationManager pushManager = new PushNotificationManager();
//    private FeedbackServiceManager feedbackManager = new FeedbackServiceManager();
//
//    public ApplePushManager() {
//    }
//
//    private static List<Device> buildDevicesList(List<MobileDevice> devices) {
//        List<MobileDevice> appleDevices = DevicesUtils.findValidAppleDevices(devices);
//        Collection<Device> collection = Collections2.transform(appleDevices, new Function<MobileDevice, Device>() {
//            @Nullable
//            @Override
//            public Device apply(@Nullable MobileDevice input) {
//                return input;
//            }
//        });
//
//        return new ArrayList<>(collection);
//    }
//
//    /**
//     * configure apple account for the sending push notification to the iphone device.
//     */
//    @Override
//    public boolean configureAccount(String configuration, String password, boolean production) {
//        boolean result = false;
//        try {
//            AppleNotificationServerBasicImpl serverBasic = new AppleNotificationServerBasicImpl(SecurityUtils.getCertificateStream(configuration), password, production);
//            pushManager.initializeConnection(serverBasic);
//            LOGGER.info("Connection initialized...");
//            LOGGER.info("done");
//            result = true;
//        } catch (Exception e) {
//            LOGGER.info(e.toString());
//        }
//        return result;
//    }
//
//    /**
//     * this method send the notification to the device and return the status of the notification.
//     */
//    @Override
//    public PushedNotifications sendMessage(@Nonnull Object message, Map<String, String> param, List<MobileDevice> devices) throws PushNotificationException {
//        PushedNotifications result = null;
//        try {
//            AppleMessage msg = buildMessage(message, param);
//
//            List<Device> devicesList = buildDevicesList(devices);
//            if (!devices.isEmpty()) {
//                if (!devicesList.isEmpty()) {
//                    pushManager.initializePreviousConnection();
//                    result = pushManager.sendNotifications(msg, devicesList);
//                    pushManager.stopConnection();
//                }
//            }
//        } catch (Exception e) {
//            throw new PushNotificationException(e);
//        }
//        return result;
//    }
//
//    private AppleMessage buildMessage(Object message, Map<String, String> param) throws Exception {
//        AppleMessage notification;
//        Preconditions.checkNotNull(message);
//        if (!(message instanceof String)) {
//            notification = (AppleMessage) message;
//        } else {
//            notification = new AppleMessage((String) message);
//        }
//        if ((param != null) && (!(param.isEmpty()))) {
//            for (Map.Entry<String, String> entry : param.entrySet()) {
//                notification.addCustomDictionary(entry.getKey(), param.get(entry.getValue()));
//            }
//        }
//        notification.checkPayloadSizeExceed();
//        return notification;
//    }
//}