package ru.trendtech.common.mobileexchange.model.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 27.01.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverRequisiteRequest {
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
