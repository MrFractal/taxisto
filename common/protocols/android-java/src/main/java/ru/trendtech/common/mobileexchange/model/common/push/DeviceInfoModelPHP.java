package ru.trendtech.common.mobileexchange.model.common.push;

public class DeviceInfoModelPHP {
    private String oldToken;
    private String newToken;
    private int deviceType; // 1 - Android, 2 - iPhone

    public String getNewToken() {
        return newToken;
    }

    public void setNewToken(String newToken) {
        this.newToken = newToken;
    }

    public String getOldToken() {
        return oldToken;
    }

    public void setOldToken(String oldToken) {
        this.oldToken = oldToken;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }
}
