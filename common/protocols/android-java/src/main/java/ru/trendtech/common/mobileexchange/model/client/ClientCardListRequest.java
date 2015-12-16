package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 16.09.2014.
 */

public class ClientCardListRequest {
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
