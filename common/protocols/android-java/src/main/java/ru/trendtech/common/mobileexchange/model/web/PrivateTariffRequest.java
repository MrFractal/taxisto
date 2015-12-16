package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 19.05.2015.
 */
public class PrivateTariffRequest {
    private String security_token;
    private long clientId;

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
