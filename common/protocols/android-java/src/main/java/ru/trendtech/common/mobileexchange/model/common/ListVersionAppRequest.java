package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 22.01.2015.
 */
public class ListVersionAppRequest {
    private String security_token;
    private String deviceType;
    //private long versionAppId;

//    public long getVersionAppId() {
//        return versionAppId;
//    }
//
//    public void setVersionAppId(long versionAppId) {
//        this.versionAppId = versionAppId;
//    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }
}
