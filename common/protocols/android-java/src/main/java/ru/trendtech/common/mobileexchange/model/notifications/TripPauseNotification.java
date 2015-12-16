package ru.trendtech.common.mobileexchange.model.notifications;

import ru.trendtech.common.mobileexchange.model.common.ItemLocation;

/**
 * Created by ivanenok on 4/14/2014.
 */
public class TripPauseNotification {
    private long missionId;
    private ItemLocation location;
    private boolean begin = false;

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

    public boolean isBegin() {
        return begin;
    }

    public void setBegin(boolean begin) {
        this.begin = begin;
    }
}
