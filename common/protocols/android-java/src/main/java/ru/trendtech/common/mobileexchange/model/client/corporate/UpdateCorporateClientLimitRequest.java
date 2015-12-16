package ru.trendtech.common.mobileexchange.model.client.corporate;

import ru.trendtech.common.mobileexchange.model.common.corporate.ClientInfoCorporate;

/**
 * Created by petr on 17.03.2015.
 */
public class UpdateCorporateClientLimitRequest {
    private String security_token;
    private LimitInfo limitInfo;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public LimitInfo getLimitInfo() {
        return limitInfo;
    }

    public void setLimitInfo(LimitInfo limitInfo) {
        this.limitInfo = limitInfo;
    }
}
