package ru.trendtech.common.mobileexchange.model.common;

import ru.trendtech.common.mobileexchange.model.common.push.DeviceInfoModel;

/**
 * Created by max on 09.04.2014.
 */
public class LogoutRequest {
    private long clientId;
    private long driverId;
    private boolean force;
    private DeviceInfoModel deviceInfo;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public DeviceInfoModel getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfoModel deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
