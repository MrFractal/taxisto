package ru.trendtech.common.mobileexchange.model.common.billing.paymobile;

/**
 * Created by petr on 15.09.2014.
 */


public class RegisterOrderClientRequest {
    private long clientId;
    private long missionId;
    private boolean answer;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }


    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
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
