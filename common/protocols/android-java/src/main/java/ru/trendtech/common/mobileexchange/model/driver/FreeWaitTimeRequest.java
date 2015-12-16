package ru.trendtech.common.mobileexchange.model.driver;

/**
 * Created by petr on 06.08.2015.
 */
public class FreeWaitTimeRequest {
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
