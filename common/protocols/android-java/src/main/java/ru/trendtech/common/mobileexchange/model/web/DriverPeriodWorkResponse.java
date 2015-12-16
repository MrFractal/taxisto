package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 03.02.2015.
 */
public class DriverPeriodWorkResponse extends ErrorCodeHelper{
    private List<DriverPeriodWorkInfo> driverPeriodWorkInfos = new ArrayList<DriverPeriodWorkInfo>();

    public List<DriverPeriodWorkInfo> getDriverPeriodWorkInfos() {
        return driverPeriodWorkInfos;
    }

    public void setDriverPeriodWorkInfos(List<DriverPeriodWorkInfo> driverPeriodWorkInfos) {
        this.driverPeriodWorkInfos = driverPeriodWorkInfos;
    }
}
