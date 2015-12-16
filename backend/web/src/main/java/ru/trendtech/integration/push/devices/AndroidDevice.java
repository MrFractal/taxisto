package ru.trendtech.integration.push.devices;

import javapns.devices.exceptions.InvalidDeviceTokenFormatException;

/**
 * File created by max on 16/04/2014 1:04.
 */


public class AndroidDevice extends MobileDevice {
    public AndroidDevice(String token, long millis) {
        super(token, token, millis);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void validate() throws InvalidDeviceTokenFormatException {

    }
}
