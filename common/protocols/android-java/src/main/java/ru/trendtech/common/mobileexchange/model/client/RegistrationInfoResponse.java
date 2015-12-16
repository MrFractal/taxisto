package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by max on 08.02.14.
 */
public class RegistrationInfoResponse {
    private long clientId;
    private Long deviceId;
    private String smsCode;


    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
