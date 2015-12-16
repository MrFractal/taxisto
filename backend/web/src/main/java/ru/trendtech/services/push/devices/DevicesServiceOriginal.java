package ru.trendtech.services.push.devices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.trendtech.common.mobileexchange.model.common.push.DeviceInfoModel;
import ru.trendtech.domain.DeviceInfo;
import ru.trendtech.models.ModelsUtils;
import ru.trendtech.repositories.DevicesRepository;
import ru.trendtech.services.TimeService;

import java.util.List;
import java.util.Set;

/**
 * Created by ivanenok on 4/14/2014.
 */
@Service
@Transactional
@Qualifier("original")
public class DevicesServiceOriginal implements DevicesService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DevicesServiceOriginal.class);

    @Autowired
    private DevicesRepository devicesRepository;

    @Autowired
    private TimeService timeService;

    @Override
    public DeviceInfo register(Set<DeviceInfo> devices, DeviceInfoModel model) {
        DeviceInfo result = null;
        if (!findAndUpdateByOld(devices, model)){
            if (!findAndUpdateByNew(devices, model)){
                result = createDeviceInfo(model);
            }
        }
        return result;
    }

    private boolean findAndUpdateByNew(Set<DeviceInfo> devices, DeviceInfoModel model) {
        boolean result = false;
        for (DeviceInfo device : devices) {
            if (device.getToken().equals(model.getNewToken())){
                device.setToken(model.getNewToken());
                device.setLastUpdate(timeService.nowDateTime());
                device.setState(DeviceInfo.State.ACTIVE);
                devicesRepository.save(device);
                result = true;
            }
        }
        return result;
    }

    private boolean findAndUpdateByOld(Set<DeviceInfo> devices, DeviceInfoModel model) {
        boolean result = false;
        for (DeviceInfo device : devices) {
            if (device.getToken().equals(model.getOldToken())){
                device.setToken(model.getNewToken());
                device.setLastUpdate(timeService.nowDateTime());
                device.setState(DeviceInfo.State.ACTIVE);
                devicesRepository.save(device);
                result = true;
            }
        }
        return result;
    }

    private DeviceInfo createDeviceInfo(DeviceInfoModel model) {
        DeviceInfo device = new DeviceInfo();
        device.setLastUpdate(timeService.nowDateTime());
        device.setState(DeviceInfo.State.ACTIVE);
        device = ModelsUtils.fromModel(model, device);
        device = devicesRepository.save(device);
        return device;
    }

    @Override
    public DeviceInfo unregister(DeviceInfoModel model) {
        DeviceInfo result = null;
        List<DeviceInfo> deviceInfos = devicesRepository.findByTokenAndType(model.getNewToken(), DeviceInfo.Type.getDeviceType(model.getDeviceType()));
        if (!deviceInfos.isEmpty()){
            if (deviceInfos.size() > 1){
                LOGGER.error("More then one device found for device token {}.", model.getNewToken());
            } else {
                LOGGER.warn("Device with token {} found.", model.getNewToken());
            }
            for (DeviceInfo deviceInfo : deviceInfos) {
                deviceInfo.setLastUpdate(timeService.nowDateTime());
                deviceInfo.setState(DeviceInfo.State.INACTIVE);
                result = devicesRepository.save(deviceInfo);
            }
        } else {
            LOGGER.warn("Device info with token {} not found!!!", model.getNewToken());
        }
        return result;
    }
}
