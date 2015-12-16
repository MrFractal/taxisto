package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 08.06.2015.
 */
public class MissionCanceledByClientRequest {
    private long startTime;
    private long endTime;
    private String security_token;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }
}
