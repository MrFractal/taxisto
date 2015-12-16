package ru.trendtech.common.mobileexchange.model.client.corporate;

/**
 * Created by petr on 13.03.2015.
 */
public class CorporateProfileRequest {
    private long clientId;
    private String security_token;

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
}
