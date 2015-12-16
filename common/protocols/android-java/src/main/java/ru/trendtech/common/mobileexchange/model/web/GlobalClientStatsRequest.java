package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 13.02.2015.
 */
public class GlobalClientStatsRequest {
    private String security_token;
    private long registrationStart;
    private long registrationEnd;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getRegistrationStart() {
        return registrationStart;
    }

    public void setRegistrationStart(long registrationStart) {
        this.registrationStart = registrationStart;
    }

    public long getRegistrationEnd() {
        return registrationEnd;
    }

    public void setRegistrationEnd(long registrationEnd) {
        this.registrationEnd = registrationEnd;
    }
}
