package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 05.11.2014.
 */

public class MdOrderClientRequest {
    private long clientId;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }
}
