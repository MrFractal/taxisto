package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 25.11.2014.
 */
public class MissionCompleteRequest {
    private long missionId;
    private int reason; // 1- crash car, 2 - crash tablet
    private long webUserId;
    private String security_token;

    public long getWebUserId() {
        return webUserId;
    }

    public void setWebUserId(long webUserId) {
        this.webUserId = webUserId;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }
}
