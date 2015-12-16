package ru.trendtech.common.mobileexchange.model.client;

import ru.trendtech.common.mobileexchange.model.common.ClientInfoPHP;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

public class LoginClientResponsePHP {
    private boolean confirmed = false;
    private String security_token;
    private long deviceId;
    private ClientInfoPHP clientInfo;
    private ErrorCodeHelper errorCodeHelper;

    public ErrorCodeHelper getErrorCodeHelper() {
        return errorCodeHelper;
    }

    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
        this.errorCodeHelper = errorCodeHelper;
    }

    public long getDeviceId() {return deviceId;}

    public String getSecurity_token() { return security_token; }

    public void setSecurity_token(String security_token) { this.security_token = security_token;}

    public void setDeviceId(long deviceId) {this.deviceId = deviceId;}

    public ClientInfoPHP getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfoPHP clientInfo) {
        this.clientInfo = clientInfo;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
