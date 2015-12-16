package ru.trendtech.common.mobileexchange.model.driver;

/**
 * Created by petr on 23.07.2015.
 */
public class DriverSystemConfigurationRequest {
    private long driverId;
    private String security_token;

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }
}
