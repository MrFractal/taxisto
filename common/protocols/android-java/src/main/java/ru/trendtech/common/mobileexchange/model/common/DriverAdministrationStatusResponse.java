package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 07.08.14.
 */


public class DriverAdministrationStatusResponse {
    private DriverLocksInfo driverLocksInfo;
    private  boolean update;

    public DriverLocksInfo getDriverLocksInfo() {
        return driverLocksInfo;
    }

    public void setDriverLocksInfo(DriverLocksInfo driverLocksInfo) {
        this.driverLocksInfo = driverLocksInfo;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}
