package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 03.11.2014.
 */
public class MoneyRefuseRequest {
    private long mdOrderId;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getMdOrderId() {
        return mdOrderId;
    }

    public void setMdOrderId(long mdOrderId) {
        this.mdOrderId = mdOrderId;
    }
}
