package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 24.08.2015.
 */
public class EmailUnsubscribeRequest {
    private long requesterId;
    private String security_token;
    private boolean unsubscribe = false;

    public long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(long requesterId) {
        this.requesterId = requesterId;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public boolean isUnsubscribe() {
        return unsubscribe;
    }

    public void setUnsubscribe(boolean unsubscribe) {
        this.unsubscribe = unsubscribe;
    }
}
