package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 12.05.2015.
 */
public class ReasonRequest {
    private String security_token;
    private long reasonId;

    public long getReasonId() {
        return reasonId;
    }

    public void setReasonId(long reasonId) {
        this.reasonId = reasonId;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }
}
