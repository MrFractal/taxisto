package ru.trendtech.common.mobileexchange.model.driver;

import ru.trendtech.common.mobileexchange.model.common.DriverRequest;

/**
 * Created by petr on 30.03.2015.
 */
public class MissionDeclinedRequest extends DriverRequest {
    private long missionId;
    private long driverId;
    private int typeReason;

    public int getTypeReason() {
        return typeReason;
    }

    public void setTypeReason(int typeReason) {
        this.typeReason = typeReason;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }
}
