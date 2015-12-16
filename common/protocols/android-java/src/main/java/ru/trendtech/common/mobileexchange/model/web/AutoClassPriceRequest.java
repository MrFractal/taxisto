package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 17.12.2014.
 */
public class AutoClassPriceRequest {
    private String security_token;
    private int autoClass;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public int getAutoClass() {
        return autoClass;
    }

    public void setAutoClass(int autoClass) {
        this.autoClass = autoClass;
    }
}
