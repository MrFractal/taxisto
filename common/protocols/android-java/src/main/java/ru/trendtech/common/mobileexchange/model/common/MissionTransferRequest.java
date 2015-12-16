package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 25.09.2014.
 */

public class MissionTransferRequest {
    private long missionId;
    private long driverIdTo;
    private String security_token;

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

    public long getDriverIdTo() {
        return driverIdTo;
    }

    public void setDriverIdTo(long driverIdTo) {
        this.driverIdTo = driverIdTo;
    }

}
