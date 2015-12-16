package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.DriverInfo;

/**
 * Created by petr on 04.09.2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverChangeResponse {
    private DriverInfo driverInfo;

    public DriverInfo getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(DriverInfo driverInfo) {
        this.driverInfo = driverInfo;
    }
}
