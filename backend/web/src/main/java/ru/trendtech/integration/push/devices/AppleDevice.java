package ru.trendtech.integration.push.devices;

import javapns.devices.exceptions.InvalidDeviceTokenFormatException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * File created by max on 16/04/2014 1:04.
 */


public class AppleDevice extends MobileDevice {
    public AppleDevice(String token) {
        this(token, 0);
    }

    public AppleDevice(String token, long millis) {
        super(token, token, millis);
    }

    @Override
    public String getDeviceId() {
        return getToken();
    }

    @Override
    public void setDeviceId(String id) {
        setToken(id);
    }

    @Override
    public boolean isValid() {
        boolean result = true;
        try {
            validate();
        } catch (InvalidDeviceTokenFormatException e) {
            result = false;
        }
        return result;
    }

    @Override
    public void validate() throws InvalidDeviceTokenFormatException {
        if (getToken() == null) {
            throw new InvalidDeviceTokenFormatException("Device Token is null, and not the required 64 bytes...");
        }
        try {
            byte[] bytes = getToken().getBytes("UTF-8");
            if (bytes.length != 64) {
                throw new InvalidDeviceTokenFormatException("Device Token has a length of [" + getToken().getBytes(Charset.defaultCharset()).length + "] and not the required 64 bytes!");
            }
        } catch (UnsupportedEncodingException e) {
            throw new InvalidDeviceTokenFormatException(getToken(), "Can't get bytes as UTF-8");
        }
    }
}
