package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.DriverInfoARM;

/**
 * Created by petr on 14.01.2015.
 */
public class DriverFindARMResponse {
    private DriverInfoARM driverInfoARM;
    private long serverState;

    public DriverInfoARM getDriverInfoARM() {
        return driverInfoARM;
    }

    public void setDriverInfoARM(DriverInfoARM driverInfoARM) {
        this.driverInfoARM = driverInfoARM;
    }

    public long getServerState() {
        return serverState;
    }

    public void setServerState(long serverState) {
        this.serverState = serverState;
    }
}
