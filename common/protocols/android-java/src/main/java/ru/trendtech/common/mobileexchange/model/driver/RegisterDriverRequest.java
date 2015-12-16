package ru.trendtech.common.mobileexchange.model.driver;

import ru.trendtech.common.mobileexchange.model.common.DriverInfo;
import ru.trendtech.common.mobileexchange.model.common.DriverInfoARM;
import ru.trendtech.common.mobileexchange.model.common.push.DeviceInfoModel;

/**
 * Created by max on 09.02.14.
 */
public class RegisterDriverRequest {
    private DriverInfoARM driverInfoARM;
    private DeviceInfoModel deviceInfoModel;

    public DeviceInfoModel getDeviceInfoModel() {
        return deviceInfoModel;
    }

    public void setDeviceInfoModel(DeviceInfoModel deviceInfoModel) {
        this.deviceInfoModel = deviceInfoModel;
    }

    public DriverInfoARM getDriverInfoARM() {
        return driverInfoARM;
    }

    public void setDriverInfoARM(DriverInfoARM driverInfoARM) {
        this.driverInfoARM = driverInfoARM;
    }
}
