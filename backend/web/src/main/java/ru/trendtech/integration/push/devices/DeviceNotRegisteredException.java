package ru.trendtech.integration.push.devices;

import ru.trendtech.integration.push.PushNotificationException;

/**
 * Created by ivanenok on 4/13/2014.
 */
class DeviceNotRegisteredException extends PushNotificationException {
    public DeviceNotRegisteredException(String msg) {
        super(msg);
    }

    public DeviceNotRegisteredException(Exception ex) {
        super(ex);
    }
}
