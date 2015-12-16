package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 27.07.2015.
 */
public class TestSendPushRequest {
    private long clientId;
    private String message;

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
