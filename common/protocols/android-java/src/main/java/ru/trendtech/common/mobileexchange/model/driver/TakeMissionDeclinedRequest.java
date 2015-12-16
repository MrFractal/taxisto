package ru.trendtech.common.mobileexchange.model.driver;

import ru.trendtech.common.mobileexchange.model.common.DriverRequest;

/**
 * Created by petr on 30.03.2015.
 */
public class TakeMissionDeclinedRequest extends DriverRequest {
    private long missionId;
    private long driverIdResponded;
    private int arrivalTime;

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public long getDriverIdResponded() {
        return driverIdResponded;
    }

    public void setDriverIdResponded(long driverIdResponded) {
        this.driverIdResponded = driverIdResponded;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
