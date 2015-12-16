package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 22.01.2015.
 */
public class CheckNewsInVersionRequest {
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
