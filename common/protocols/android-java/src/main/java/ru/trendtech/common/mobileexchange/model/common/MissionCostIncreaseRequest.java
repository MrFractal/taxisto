package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 29.08.14.
 */

public class MissionCostIncreaseRequest {
    private long missionId;
    private Double sumIncrease;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }


    public Double getSumIncrease() {
        return sumIncrease;
    }

    public void setSumIncrease(Double sumIncrease) {
        this.sumIncrease = sumIncrease;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }
}
