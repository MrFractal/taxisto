package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 22.01.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VersionsAppInfo {
    private long id;
    private String version;
    private int statusVersion;
    private String clientType;
    private String showTarif;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getStatusVersion() {
        return statusVersion;
    }

    public void setStatusVersion(int statusVersion) {
        this.statusVersion = statusVersion;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getShowTarif() {
        return showTarif;
    }

    public void setShowTarif(String showTarif) {
        this.showTarif = showTarif;
    }
}
