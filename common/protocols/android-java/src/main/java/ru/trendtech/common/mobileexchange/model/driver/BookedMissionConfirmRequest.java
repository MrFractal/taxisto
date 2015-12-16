package ru.trendtech.common.mobileexchange.model.driver;

/**
 * File created by max on 07/05/2014 20:02.
 */

public class BookedMissionConfirmRequest {
    private long missionId;
    private boolean decline = false;

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public boolean isDecline() {
        return decline;
    }

    public void setDecline(boolean decline) {
        this.decline = decline;
    }
}
