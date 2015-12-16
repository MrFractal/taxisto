package ru.trendtech.common.mobileexchange.model.client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 05.08.14.
 */



public class RatesDriverResponse {
    private long driverId;
    private List<RateDriverResponse> driverRates=new ArrayList<RateDriverResponse>();

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public List getDriverRates() {
        return driverRates;
    }

    public void setDriverRates(List driverRates) {
        this.driverRates = driverRates;
    }
}
