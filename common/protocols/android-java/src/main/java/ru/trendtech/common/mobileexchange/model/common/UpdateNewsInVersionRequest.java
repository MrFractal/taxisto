package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 22.01.2015.
 */
public class UpdateNewsInVersionRequest {
    private NewsVersionAppInfo newsVersionAppInfo;
    private String security_token;

    public NewsVersionAppInfo getNewsVersionAppInfo() {
        return newsVersionAppInfo;
    }

    public void setNewsVersionAppInfo(NewsVersionAppInfo newsVersionAppInfo) {
        this.newsVersionAppInfo = newsVersionAppInfo;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }
}
