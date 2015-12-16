package ru.trendtech.common.mobileexchange.model.web;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 03.02.2015.
 */
public class UpdateDriverPeriodWorkRequest {
    private String security_token;
    private List<DriverPeriodWorkInfo> driverPeriodWorkInfos = new ArrayList<DriverPeriodWorkInfo>();

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public List<DriverPeriodWorkInfo> getDriverPeriodWorkInfos() {
        return driverPeriodWorkInfos;
    }

    public void setDriverPeriodWorkInfos(List<DriverPeriodWorkInfo> driverPeriodWorkInfos) {
        this.driverPeriodWorkInfos = driverPeriodWorkInfos;
    }
}
