package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by ivanenok on 4/14/2014.
 */
public class ClientRequest {
    private long clientId;

    public ClientRequest() {
    }

    public ClientRequest(long clientId) {
        this.clientId = clientId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }
}
