package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 29.04.2015.
 */
public class MultipleMissionInfo {
    private MissionInfo missionInfo;
    private String timeOfStarting;
    private int autoCount;

    public String getTimeOfStarting() {
        return timeOfStarting;
    }

    public void setTimeOfStarting(String timeOfStarting) {
        this.timeOfStarting = timeOfStarting;
    }

    public MissionInfo getMissionInfo() {
        return missionInfo;
    }

    public void setMissionInfo(MissionInfo missionInfo) {
        this.missionInfo = missionInfo;
    }

    public int getAutoCount() {
        return autoCount;
    }

    public void setAutoCount(int autoCount) {
        this.autoCount = autoCount;
    }
}
