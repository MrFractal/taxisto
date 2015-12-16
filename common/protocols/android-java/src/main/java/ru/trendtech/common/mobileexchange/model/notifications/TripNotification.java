package ru.trendtech.common.mobileexchange.model.notifications;

public class TripNotification {
    private long missionId;
    private boolean begin = false;

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public boolean isBegin() {
        return begin;
    }

    public void setBegin(boolean begin) {
        this.begin = begin;
    }
}
