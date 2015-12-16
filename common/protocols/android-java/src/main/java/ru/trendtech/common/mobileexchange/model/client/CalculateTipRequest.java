package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 09.02.2015.
 */
public class CalculateTipRequest {
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
