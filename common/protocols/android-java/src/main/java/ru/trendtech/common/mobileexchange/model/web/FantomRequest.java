package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 29.06.2015.
 */
public class FantomRequest {
    private String security_token;
    private long driverId;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }
}
