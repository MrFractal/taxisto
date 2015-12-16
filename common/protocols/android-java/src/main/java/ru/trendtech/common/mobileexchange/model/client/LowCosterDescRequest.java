package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 11.03.2015.
 */
public class LowCosterDescRequest {
    private String security_token;
    private long clientId;

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }
}
