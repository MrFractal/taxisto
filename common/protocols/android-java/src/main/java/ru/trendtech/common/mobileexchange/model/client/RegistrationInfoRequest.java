package ru.trendtech.common.mobileexchange.model.client;

import ru.trendtech.common.mobileexchange.model.common.ClientInfo;
import ru.trendtech.common.mobileexchange.model.common.push.DeviceInfoModel;

/**
 * Created by max on 06.02.14.
 */
public class RegistrationInfoRequest {
    private ClientInfo clientInfo;
    private DeviceInfoModel deviceInfoModel;


    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public DeviceInfoModel getDeviceInfoModel() {
        return deviceInfoModel;
    }

    public void setDeviceInfoModel(DeviceInfoModel deviceInfoModel) {
        this.deviceInfoModel = deviceInfoModel;
    }
}
