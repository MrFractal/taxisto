package ru.trendtech.services.sms;

/**
 * Created by max on 15.02.14.
 */
public interface GatewaySMS {
    public boolean send(String phone, String message, String messageId);
}
