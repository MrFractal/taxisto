package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 22.01.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsVersionAppInfo {
    private long id;
    private String url;
    private boolean active;
    private VersionsAppInfo versionsAppInfo;

    public VersionsAppInfo getVersionsAppInfo() {
        return versionsAppInfo;
    }

    public void setVersionsAppInfo(VersionsAppInfo versionsAppInfo) {
        this.versionsAppInfo = versionsAppInfo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
