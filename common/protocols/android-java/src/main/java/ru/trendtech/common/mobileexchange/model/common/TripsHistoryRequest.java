package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by max on 06.02.14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TripsHistoryRequest {
    private long requesterId;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public TripsHistoryRequest(long requesterId) {
        this.requesterId = requesterId;
    }

    public TripsHistoryRequest() {
       super();
    }

    public long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(long requesterId) {
        this.requesterId = requesterId;
    }
}
