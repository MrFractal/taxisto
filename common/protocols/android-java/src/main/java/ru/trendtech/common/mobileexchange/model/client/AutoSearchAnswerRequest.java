package ru.trendtech.common.mobileexchange.model.client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 05.05.2015.
 */
public class AutoSearchAnswerRequest {
    private long clientId;
    private String security_token;
    private long missionId;
    private boolean answer = false;

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
