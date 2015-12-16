package ru.trendtech.common.mobileexchange.model.client.corporate;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 13.03.2015.
 */
public class CorporateLoginResponse extends ErrorCodeHelper {
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
