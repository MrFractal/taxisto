package ru.trendtech.common.mobileexchange.model.client.corporate;

/**
 * Created by petr on 16.03.2015.
 */
public class CorporateClientRequest {
    private String security_token;
    private long mainClientId;

    public long getMainClientId() {
        return mainClientId;
    }

    public void setMainClientId(long mainClientId) {
        this.mainClientId = mainClientId;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }
}
