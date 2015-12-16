package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 07.11.2014.
 */

public class CheckVersionRequest {
   private String version;
   private String clientType;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
}
