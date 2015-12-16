package ru.trendtech.integration.push.devices;

import javapns.devices.Device;
import javapns.devices.exceptions.InvalidDeviceTokenFormatException;

import java.sql.Timestamp;

/**
 * File created by petr on 16/04/2014 1:05.
 */


public abstract class MobileDevice implements Device {
    private String deviceId;
    private String token;
    private Timestamp lastRegister;

    public MobileDevice(String deviceId, String token, long millis) {
        this.deviceId = deviceId;
        this.token = token;
        this.lastRegister = new Timestamp(millis);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getLastRegister() {
        return new Timestamp(lastRegister.getTime());
    }

    public void setLastRegister(Timestamp lastRegister) {
        this.lastRegister = new Timestamp(lastRegister.getTime());
    }

    public abstract boolean isValid();

    public abstract void validate() throws InvalidDeviceTokenFormatException;
}
