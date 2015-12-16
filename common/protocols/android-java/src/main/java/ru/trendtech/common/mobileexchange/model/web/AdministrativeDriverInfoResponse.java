package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.DriverInfo;

/**
 * File created by max on 09/06/2014 21:35.
 */


public class AdministrativeDriverInfoResponse {
    private DriverInfo driverInfo;
    private long serverState;

    public AdministrativeDriverInfoResponse() {
    }

    public DriverInfo getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(DriverInfo driverInfo) {
        this.driverInfo = driverInfo;
    }

    public long getServerState() {
        return serverState;
    }

    public void setServerState(long serverState) {
        this.serverState = serverState;
    }
}
