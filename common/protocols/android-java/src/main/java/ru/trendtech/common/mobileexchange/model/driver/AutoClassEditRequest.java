package ru.trendtech.common.mobileexchange.model.driver;

/**
 * Created by petr on 29.08.14.
 */
public class AutoClassEditRequest {

    private long driverId;
    private int autoClass;

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public int getAutoClass() {
        return autoClass;
    }

    public void setAutoClass(int autoClass) {
        this.autoClass = autoClass;
    }
}
