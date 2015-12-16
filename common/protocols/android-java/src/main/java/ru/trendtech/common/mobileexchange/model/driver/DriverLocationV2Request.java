package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.DriverRequest;
import ru.trendtech.common.mobileexchange.model.common.ItemLocation;

/**
 * Created by petr on 02.02.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverLocationV2Request extends DriverRequest {
    private ItemLocation location;
    private int distance;
    private long missionId;
    private int timeWork;
    private int timeRest;
    private int timePayRest;

    public ItemLocation getLocation() {
        return location;
    }

    public void setLocation(ItemLocation location) {
        this.location = location;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public int getTimeWork() {
        return timeWork;
    }

    public void setTimeWork(int timeWork) {
        this.timeWork = timeWork;
    }

    public int getTimeRest() {
        return timeRest;
    }

    public void setTimeRest(int timeRest) {
        this.timeRest = timeRest;
    }

    public int getTimePayRest() {
        return timePayRest;
    }

    public void setTimePayRest(int timePayRest) {
        this.timePayRest = timePayRest;
    }
}
