package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 03.04.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverSettingResponse extends ErrorCodeHelper {
    private DriverSettingInfo driverSettingInfo;

    public DriverSettingInfo getDriverSettingInfo() {
        return driverSettingInfo;
    }

    public void setDriverSettingInfo(DriverSettingInfo driverSettingInfo) {
        this.driverSettingInfo = driverSettingInfo;
    }
}
