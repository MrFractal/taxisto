package ru.trendtech.common.mobileexchange.model.common;

import ru.trendtech.common.mobileexchange.model.common.push.DeviceInfoModel;

public class LoginRequestPHP {
    private String login;
    private String password;
    private String version;
    private DeviceInfoModel deviceInfoModel;

    public LoginRequestPHP() {
    }

    public LoginRequestPHP(String login, String password, DeviceInfoModel deviceInfoModel) {
        this.login = login;
        this.password = password;
        this.deviceInfoModel = deviceInfoModel;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
