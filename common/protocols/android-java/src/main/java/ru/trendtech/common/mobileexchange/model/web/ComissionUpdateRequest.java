package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ComissionInfo;

/**
 * Created by petr on 17.12.2014.
 */
public class ComissionUpdateRequest {
    private String security_token;
    private ComissionInfo comissionInfo;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public ComissionInfo getComissionInfo() {
        return comissionInfo;
    }

    public void setComissionInfo(ComissionInfo comissionInfo) {
        this.comissionInfo = comissionInfo;
    }
}
