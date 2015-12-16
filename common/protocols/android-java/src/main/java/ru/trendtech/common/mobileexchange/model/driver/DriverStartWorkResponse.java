package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.DriverStartWorkInfo;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 30.01.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverStartWorkResponse extends ErrorCodeHelper{
    private DriverStartWorkInfo driverStartWorkInfo;

    public DriverStartWorkInfo getDriverStartWorkInfo() {
        return driverStartWorkInfo;
    }

    public void setDriverStartWorkInfo(DriverStartWorkInfo driverStartWorkInfo) {
        this.driverStartWorkInfo = driverStartWorkInfo;
    }
}
