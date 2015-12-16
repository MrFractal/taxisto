package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 15.09.2014.
 */

public class RegisterOrderRequest {
    private long driverId;
    private long missionId;

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }
}
