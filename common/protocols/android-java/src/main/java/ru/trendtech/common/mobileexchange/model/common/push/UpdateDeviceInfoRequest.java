package ru.trendtech.common.mobileexchange.model.common.push;

/**
 * Created by ivanenok on 4/4/2014.
 */
public class UpdateDeviceInfoRequest {
    private long requesterId;
    private DeviceInfoModel deviceInfoModel;

    public long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(long requesterId) {
        this.requesterId = requesterId;
    }

    public DeviceInfoModel getDeviceInfoModel() {
        return deviceInfoModel;
    }

    public void setDeviceInfoModel(DeviceInfoModel deviceInfoModel) {
        this.deviceInfoModel = deviceInfoModel;
    }
}
