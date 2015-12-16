package ru.trendtech.services.security;

import org.joda.time.DateTime;

/**
 * File created by max on 10/06/2014 7:09.
 */


public class SecurityToken {
    private DateTime creationTime;
    private long userId;
    private String token;

    public DateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(DateTime creationTime) {
        this.creationTime = creationTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
