package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 21.01.2015.
 */
public class AssigningDriverToBookingRequest {
    private String security_token;
    private long missionId;
    private long driverId;

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

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }
}
