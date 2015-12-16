package ru.trendtech.common.mobileexchange.model.common;

/**
 * File created by max on 06/05/2014 21:12.
 */


public class ActivateBonusCardRequest {
    private long id;
    private String text;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
