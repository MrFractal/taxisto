package ru.trendtech.common.mobileexchange.model.common.sms;

/**
 * File created by max on 08/05/2014 0:42.
 */


public class SendSMSRequest {
    public static final int DRIVER_WAIT = 1;
    public static final int DRIVER_WAIT_TOO_LONG = 2;
    public static final int CLIENT_DRIVER_NOT_FOUND = 3;

    private int type;
    private long senderId;
    private String phone;
    private String message;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
