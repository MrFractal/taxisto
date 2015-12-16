package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 18.09.2014.
 */
public class SmsSendDefaultResponse {
    private boolean sendSms = false;

    public boolean isSendSms() {
        return sendSms;
    }

    public void setSendSms(boolean sendSms) {
        this.sendSms = sendSms;
    }
}
