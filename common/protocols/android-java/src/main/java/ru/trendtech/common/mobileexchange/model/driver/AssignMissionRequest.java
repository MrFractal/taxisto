package ru.trendtech.common.mobileexchange.model.driver;

import ru.trendtech.common.mobileexchange.model.common.DriverRequest;
import ru.trendtech.common.mobileexchange.model.common.ItemLocation;

/**
 * Created by max on 06.02.14.
 */
public class AssignMissionRequest extends DriverRequest {
    private long missionId;

    private int arrivalTime;

    private ItemLocation location;

    public AssignMissionRequest() {
        super();
    }

    public AssignMissionRequest(long driverId, long missionId, int arrivalTime, ItemLocation location) {
        super(driverId);
        this.missionId = missionId;
        this.arrivalTime = arrivalTime;
        this.location = location;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public ItemLocation getLocation() {
        return location;
    }

    public void setLocation(ItemLocation location) {
        this.location = location;
    }
}
