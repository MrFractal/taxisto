package ru.trendtech.services.push.devices;

import ru.trendtech.common.mobileexchange.model.common.push.DeviceInfoModel;
import ru.trendtech.domain.DeviceInfo;

import java.util.Set;

/**
 * File created by max on 21/06/2014 2:22.
 */


public interface DevicesService {
    DeviceInfo register(Set<DeviceInfo> devices, DeviceInfoModel model);

    DeviceInfo unregister(DeviceInfoModel model);
}
