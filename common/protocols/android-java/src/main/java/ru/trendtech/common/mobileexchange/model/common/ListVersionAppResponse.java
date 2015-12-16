package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 22.01.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListVersionAppResponse {
    private List<VersionsAppInfo> versionsAppInfos = new ArrayList<VersionsAppInfo>();

    public List<VersionsAppInfo> getVersionsAppInfos() {
        return versionsAppInfos;
    }

    public void setVersionsAppInfos(List<VersionsAppInfo> versionsAppInfos) {
        this.versionsAppInfos = versionsAppInfos;
    }
}
