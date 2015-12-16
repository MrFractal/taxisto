package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 16.09.2014.
 */

public class DeleteClientCardRequest {
    private long clientCardId;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getClientCardId() {
        return clientCardId;
    }

    public void setClientCardId(long clientCardId) {
        this.clientCardId = clientCardId;
    }
}
