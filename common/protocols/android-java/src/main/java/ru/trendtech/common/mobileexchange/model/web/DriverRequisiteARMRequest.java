package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 26.03.2015.
 */
public class DriverRequisiteARMRequest {
    private String security_token;
    private long driverId;
    private long driverRequisiteId;

    public long getDriverRequisiteId() {
        return driverRequisiteId;
    }

    public void setDriverRequisiteId(long driverRequisiteId) {
        this.driverRequisiteId = driverRequisiteId;
    }

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
