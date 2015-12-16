package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.DriverInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * File created by max on 21/06/2014 9:00.
 */


public class FindDriversResponse {
    private List<DriverInfo> drivers = new ArrayList<>();

    public List<DriverInfo> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<DriverInfo> drivers) {
        this.drivers = drivers;
    }
}
