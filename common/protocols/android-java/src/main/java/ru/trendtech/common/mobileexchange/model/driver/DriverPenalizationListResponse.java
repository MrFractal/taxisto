package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.DriverCash;

import java.util.List;

/**
 * Created by petr on 02.09.14.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverPenalizationListResponse {
    private List<DriverCash> driverPenalizationCashList;

    public List<DriverCash> getDriverPenalizationCashList() {
        return driverPenalizationCashList;
    }

    public void setDriverPenalizationCashList(List<DriverCash> driverPenalizationCashList) {
        this.driverPenalizationCashList = driverPenalizationCashList;
    }
}
