package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 29.09.2014.
 */
public class SMSCodeRepeateTerminalRequest {
   private String phone;
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
}
