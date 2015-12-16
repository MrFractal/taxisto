package ru.trendtech.common.mobileexchange.model.common;

import java.util.List;

/**
 * Created by petr on 08.08.14.
 */
public class DriversListResponse {
    private long lastPageNumber;
    private List<DriverInfoARM> driverInfoARM;
    private Long totalItems;

    public long getLastPageNumber() {
        return lastPageNumber;
    }

    public void setLastPageNumber(long lastPageNumber) {
        this.lastPageNumber = lastPageNumber;
    }

    public List<DriverInfoARM> getDriverInfoARM() {
        return driverInfoARM;
    }

    public void setDriverInfoARM(List<DriverInfoARM> driverInfoARM) {
        this.driverInfoARM = driverInfoARM;
    }

    public Long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Long totalItems) {
        this.totalItems = totalItems;
    }
}
