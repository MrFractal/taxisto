package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 06.08.2015.
 */
public class BukanovReportRequest {
    private String security_token;
    private long startTime;
    private long endTime;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

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
}
