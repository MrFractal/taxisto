package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 16.01.2015.
 */
public class DriverRatingARMRequest {
    private int driverId;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }
}
