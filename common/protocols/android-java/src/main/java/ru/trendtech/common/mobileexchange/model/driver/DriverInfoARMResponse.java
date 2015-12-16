package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.DriverInfoARM;

/**
 * Created by petr on 14.10.2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverInfoARMResponse {
    private DriverInfoARM driverInfoARM;

    public DriverInfoARM getDriverInfoARM() {
        return driverInfoARM;
    }

    public void setDriverInfoARM(DriverInfoARM driverInfoARM) {
        this.driverInfoARM = driverInfoARM;
    }
}
