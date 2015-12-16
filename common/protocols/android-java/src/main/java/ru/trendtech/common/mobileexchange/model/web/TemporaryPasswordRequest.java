package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 29.12.2014.
 */
public class TemporaryPasswordRequest {
    private String phone;
    private long clientId;
    private String security_token;
    private long webUserId;

    public long getWebUserId() {
        return webUserId;
    }

    public void setWebUserId(long webUserId) {
        this.webUserId = webUserId;
    }

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

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }
}
