package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.RegionInfo;

/**
 * Created by petr on 29.03.2015.
 */
public class CheckPointInsidePolygonResponse extends ErrorCodeHelper {
    private RegionInfo regionInfo;

    public RegionInfo getRegionInfo() {
        return regionInfo;
    }

    public void setRegionInfo(RegionInfo regionInfo) {
        this.regionInfo = regionInfo;
    }
}
