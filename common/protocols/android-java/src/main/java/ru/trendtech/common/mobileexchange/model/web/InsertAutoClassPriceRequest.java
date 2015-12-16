package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.rates.AutoClassRateInfoV2;

/**
 * Created by petr on 26.12.2014.
 */
public class InsertAutoClassPriceRequest {
    private String security_token;
    private AutoClassRateInfoV2 autoClassRateInfoV2;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public AutoClassRateInfoV2 getAutoClassRateInfoV2() {
        return autoClassRateInfoV2;
    }

    public void setAutoClassRateInfoV2(AutoClassRateInfoV2 autoClassRateInfoV2) {
        this.autoClassRateInfoV2 = autoClassRateInfoV2;
    }
}
