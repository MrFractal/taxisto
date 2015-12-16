package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.DriverActivityInfo;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 31.03.2015.
 */
public class ActivityDriverResponse extends ErrorCodeHelper {
    private List<DriverActivityInfo> driverActivityInfos = new ArrayList<DriverActivityInfo>();
//    private long totalItems;
//    private int lastPageNumber;

    public List<DriverActivityInfo> getDriverActivityInfos() {
        return driverActivityInfos;
    }

    public void setDriverActivityInfos(List<DriverActivityInfo> driverActivityInfos) {
        this.driverActivityInfos = driverActivityInfos;
    }
}
