package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 26.12.2014.
 */
public class AutoSearchCancelRequest {
    private long clientId;
    private String security_token;
    private long missionId;

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
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
