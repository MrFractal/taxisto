package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 21.10.2014.
 */
public class PaymentLetterRequest {
    private long missionId;
    private String security_token;
    private boolean toClient;

    public boolean isToClient() {
        return toClient;
    }

    public void setToClient(boolean toClient) {
        this.toClient = toClient;
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
