package ru.trendtech.common.mobileexchange.model.web;

/**
 * File created by max on 09/06/2014 20:17.
 */


public class AdministratorLoginResponse {
    private long adminId;
    private WebUserModel userModel = new WebUserModel();
    private WebUserConfigurationModel configurationModel = new WebUserConfigurationModel();
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

    public WebUserConfigurationModel getConfigurationModel() {
        return configurationModel;
    }

    public void setConfigurationModel(WebUserConfigurationModel configurationModel) {
        this.configurationModel = configurationModel;
    }


    public WebUserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(WebUserModel userModel) {
        this.userModel = userModel;
    }
}
