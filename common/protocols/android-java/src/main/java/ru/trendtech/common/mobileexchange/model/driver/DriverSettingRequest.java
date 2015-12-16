package ru.trendtech.common.mobileexchange.model.driver;

/**
 * Created by petr on 03.04.2015.
 */
public class DriverSettingRequest {
    private long driverId;
    private String security_token;

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
