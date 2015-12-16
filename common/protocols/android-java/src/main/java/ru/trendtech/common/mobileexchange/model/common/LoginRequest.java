package ru.trendtech.common.mobileexchange.model.common;

import ru.trendtech.common.mobileexchange.model.common.push.DeviceInfoModel;

public class LoginRequest {
    private String login;
    private String password;
    private DeviceInfoModel deviceInfoModel;
    private String versionApp;


    public LoginRequest() {
    }

    public LoginRequest(String login, String password, DeviceInfoModel deviceInfoModel, String versionApp) {
        this.login = login;
        this.password = password;
        this.deviceInfoModel = deviceInfoModel;
        this.versionApp = versionApp;
    }


    public String getVersionApp() {
        return versionApp;
    }

    public void setVersionApp(String versionApp) {
        this.versionApp = versionApp;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DeviceInfoModel getDeviceInfoModel() {
        return deviceInfoModel;
    }

    public void setDeviceInfoModel(DeviceInfoModel deviceInfoModel) {
        this.deviceInfoModel = deviceInfoModel;
    }
}
