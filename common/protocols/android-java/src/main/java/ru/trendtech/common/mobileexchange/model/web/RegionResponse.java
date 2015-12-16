package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.RegionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 22.04.2015.
 */
public class RegionResponse extends ErrorCodeHelper {
    private List<RegionInfo> regionInfos = new ArrayList<RegionInfo>();

    public List<RegionInfo> getRegionInfos() {
        return regionInfos;
    }

    public void setRegionInfos(List<RegionInfo> regionInfos) {
        this.regionInfos = regionInfos;
    }
}
