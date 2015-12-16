package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 08.05.2015.
 */
public class CurrentStateMissionRequest {
    private String security_token;
    private long clientId;
    private long missionId;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }
}
