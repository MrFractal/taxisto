package ru.trendtech.common.mobileexchange.model.driver;

import ru.trendtech.common.mobileexchange.model.common.DriverRequest;
import ru.trendtech.common.mobileexchange.model.common.ItemLocation;

public class DriverLocationRequestPHP extends DriverRequest {
    private ItemLocation location;

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    private long driverId;
    private int distance;
    private long missionId;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(int missionId) {
        this.missionId = missionId;
    }

    public ItemLocation getLocation() {
        return location;
    }

    public void setLocation(ItemLocation location) {
        this.location = location;
    }
}
