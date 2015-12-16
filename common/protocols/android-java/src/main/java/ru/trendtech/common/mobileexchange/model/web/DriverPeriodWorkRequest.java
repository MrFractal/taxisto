package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 03.02.2015.
 */
public class DriverPeriodWorkRequest {
    private long driverPeriodWorkId;
    private long driverId;
    private long startTime;
    private long endTime;
    private Boolean active;
    private String security_token;

    public long getDriverPeriodWorkId() {
        return driverPeriodWorkId;
    }

    public void setDriverPeriodWorkId(long driverPeriodWorkId) {
        this.driverPeriodWorkId = driverPeriodWorkId;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }
}
