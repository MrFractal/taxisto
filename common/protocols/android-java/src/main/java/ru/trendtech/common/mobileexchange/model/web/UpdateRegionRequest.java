package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.RegionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 15.04.2015.
 */
public class UpdateRegionRequest {
    private String security_token;
    private List<RegionInfo> regionInfos = new ArrayList<RegionInfo>();

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public List<RegionInfo> getRegionInfos() {
        return regionInfos;
    }

    public void setRegionInfos(List<RegionInfo> regionInfos) {
        this.regionInfos = regionInfos;
    }
}
