package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 15.01.2015.
 */
public class MissionInfoARMRequest {
    private long missionId;
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
}
