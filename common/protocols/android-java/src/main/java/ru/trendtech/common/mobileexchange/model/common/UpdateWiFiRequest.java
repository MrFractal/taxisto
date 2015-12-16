package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 01.10.2014.
 */
public class UpdateWiFiRequest {
    private boolean status;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
