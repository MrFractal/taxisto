package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 11.06.2015.
 */
public class ServicePriceRequest {
    private String security_token;
    private long requesterId;

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
}
