package ru.trendtech.services.push.devices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.trendtech.common.mobileexchange.model.common.push.DeviceInfoModel;
import ru.trendtech.domain.DeviceInfo;
import ru.trendtech.repositories.DevicesRepository;
import ru.trendtech.services.TimeService;

import java.util.List;
import java.util.Set;

/**
 * File created by max on 21/06/2014 2:25.
 */

@Service
@Qualifier("exclusive")
public class DevicesServiceExclusive implements DevicesService {

    @Autowired
    private DevicesRepository devicesRepository;

    @Autowired
    private TimeService timeService;

    @Override
    public DeviceInfo register(Set<DeviceInfo> devices, DeviceInfoModel model) {
        devicesRepository.delete(devices);
        DeviceInfo device = new DeviceInfo();
        device.setToken(model.getNewToken());
        device.setType(DeviceInfo.Type.getDeviceType(model.getDeviceType()));
        device.setLastUpdate(timeService.nowDateTime());
        device.setState(DeviceInfo.State.ACTIVE);
        return devicesRepository.save(device);
    }

    @Override
    public DeviceInfo unregister(DeviceInfoModel model) {
        if(model != null) {
            if (StringUtils.isEmpty(model.getNewToken())) {
                deleteByTokenAnDevice(model.getNewToken(), model.getDeviceType());
            }
            if (StringUtils.isEmpty(model.getOldToken())) {
                deleteByTokenAnDevice(model.getOldToken(), model.getDeviceType());
            }
            if (StringUtils.isEmpty(model.getToken())) {
                deleteByTokenAnDevice(model.getToken(), model.getDeviceType());
            }
        }
        return null;
    }

    private void deleteByTokenAnDevice(String token, int type) {
        List<DeviceInfo> deviceInfos = devicesRepository.findByTokenAndType(token, DeviceInfo.Type.getDeviceType(type));
        devicesRepository.delete(deviceInfos);
    }
}
