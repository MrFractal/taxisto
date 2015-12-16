package ru.trendtech.integration.push;

/**
 * Created by ivanenok on 4/13/2014.
 */
public class PushNotificationException extends Exception {
    public PushNotificationException(String msg) {
        super(msg);
    }

    public PushNotificationException(Exception ex) {
        super(ex);
    }
}
