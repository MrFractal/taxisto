package ru.trendtech.common.mobileexchange.model.driver;

import ru.trendtech.common.mobileexchange.model.common.DriverInfo;

/**
 * Created by petr on 04.09.2014.
 */
public class DriverChangeRequest {
    private DriverInfo driverInfo;

    public DriverInfo getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(DriverInfo driverInfo) {
        this.driverInfo = driverInfo;
    }
}
