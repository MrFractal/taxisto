package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 19.09.2014.
 */


public class InsertPartnersGroupRequest {
    private PartnersGroupInfo partnersGroupInfo;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public PartnersGroupInfo getPartnersGroupInfo() {
        return partnersGroupInfo;
    }

    public void setPartnersGroupInfo(PartnersGroupInfo partnersGroupInfo) {
        this.partnersGroupInfo = partnersGroupInfo;
    }
}
