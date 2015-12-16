package ru.trendtech.common.mobileexchange.model.driver;

import ru.trendtech.common.mobileexchange.model.common.DriverRequest;
import ru.trendtech.common.mobileexchange.model.common.ItemLocation;

/**
 * Created by max on 09.02.14.
 */
public class DriverLocationRequest extends DriverRequest {
    private ItemLocation location;
    private int distance;
    private int missionId;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getMissionId() {
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
