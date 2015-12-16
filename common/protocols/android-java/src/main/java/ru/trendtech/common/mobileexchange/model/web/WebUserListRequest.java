package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 23.03.2015.
 */
public class WebUserListRequest {
    private String security_token;
    private long webUserId;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getWebUserId() {
        return webUserId;
    }

    public void setWebUserId(long webUserId) {
        this.webUserId = webUserId;
    }
}
