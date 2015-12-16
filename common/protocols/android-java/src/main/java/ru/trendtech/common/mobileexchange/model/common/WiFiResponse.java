package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 01.10.2014.
 */

public class WiFiResponse {
   private boolean wifiStatus;
   private ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();

    public ErrorCodeHelper getErrorCodeHelper() {
        return errorCodeHelper;
    }

    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
        this.errorCodeHelper = errorCodeHelper;
    }

    public boolean isWifiStatus() {
        return wifiStatus;
    }

    public void setWifiStatus(boolean wifiStatus) {
        this.wifiStatus = wifiStatus;
    }
}
