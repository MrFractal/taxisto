package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 01.10.2014.
 */
public class SmsSendMissionInfoRequest {
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
