package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.DriverCashFlowInfo;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 10.02.2015.
 */
public class DriverCashFlowResponse extends ErrorCodeHelper {
    private List<DriverCashFlowInfo> driverCashFlowInfos = new ArrayList<DriverCashFlowInfo>();
    private long lastPageNumber;
    private long totalItems;

    public List<DriverCashFlowInfo> getDriverCashFlowInfos() {
        return driverCashFlowInfos;
    }

    public void setDriverCashFlowInfos(List<DriverCashFlowInfo> driverCashFlowInfos) {
        this.driverCashFlowInfos = driverCashFlowInfos;
    }

    public long getLastPageNumber() {
        return lastPageNumber;
    }

    public void setLastPageNumber(long lastPageNumber) {
        this.lastPageNumber = lastPageNumber;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }
}
