package ru.trendtech.common.mobileexchange.model.web.corporate;

/**
 * Created by petr on 26.03.2015.
 */
public class CorporateClientARMRequest {
    private String security_token;
    private long mainClientId;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getMainClientId() {
        return mainClientId;
    }

    public void setMainClientId(long mainClientId) {
        this.mainClientId = mainClientId;
    }
}
