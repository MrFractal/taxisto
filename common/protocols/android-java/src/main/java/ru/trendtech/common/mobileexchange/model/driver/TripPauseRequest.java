package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.DriverRequest;
import ru.trendtech.common.mobileexchange.model.common.ItemLocation;

/**
 * Created by max on 13.02.14.
 */
public class TripPauseRequest extends DriverRequest {
    private long missionId;
    private ItemLocation location;
    private boolean pauseBegin;
    private int pauseTime;

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public ItemLocation getLocation() {
        return location;
    }

    public void setLocation(ItemLocation location) {
        this.location = location;
    }

    public boolean isPauseBegin() {
        return pauseBegin;
    }

    public void setPauseBegin(boolean pauseBegin) {
        this.pauseBegin = pauseBegin;
    }

    public int getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(int pauseTime) {
        this.pauseTime = pauseTime;
    }
}
