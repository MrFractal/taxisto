package ru.trendtech.integration.push.manager;

import ru.trendtech.integration.push.PushNotificationException;
import ru.trendtech.integration.push.devices.MobileDevice;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by ivanenok on 4/4/2014.
 */
public abstract class PushManager<T> {
    /**
     * for configure the push notification accounts instance or to register the services over the push notification services like APNS OR GCM
     *
     * @param configuration push manager specific for system
     * @param password      password
     * @return String
     */
    public abstract boolean configureAccount(String configuration, String password, boolean production);

    public T sendMessage(Object message, MobileDevice... devices) throws PushNotificationException {
        return sendMessage(message, Arrays.asList(devices));
    }

    public T sendMessage(Object message, List<MobileDevice> devices) throws PushNotificationException {
        Map<String, String> emptyMap = Collections.emptyMap();
        return sendMessage(message, emptyMap, devices);
    }

    public T sendMessage(Object message, Map<String, String> param, MobileDevice... devices) throws PushNotificationException {
        return sendMessage(message, param, Arrays.asList(devices));
    }

    public abstract T sendMessage(Object message, Map<String, String> param, List<MobileDevice> devices) throws PushNotificationException;
}