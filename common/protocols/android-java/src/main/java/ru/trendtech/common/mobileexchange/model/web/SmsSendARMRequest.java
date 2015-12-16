package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 15.01.2015.
 */


public class SmsSendARMRequest {
    private String phone;
    private String message;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
