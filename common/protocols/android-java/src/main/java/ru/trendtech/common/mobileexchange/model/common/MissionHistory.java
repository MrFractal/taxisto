package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 24.11.2014.
 */
public class MissionHistory {
    private MissionInfo missionInfo;
    private String timeRequesting;
    private String timeStarting;
    private String timeNow;
    private String autoClassStr;

    public String getAutoClassStr() {
        return autoClassStr;
    }

    public void setAutoClassStr(String autoClassStr) {
        this.autoClassStr = autoClassStr;
    }

    public String getTimeNow() {
        return timeNow;
    }

    public void setTimeNow(String timeNow) {
        this.timeNow = timeNow;
    }

    public MissionInfo getMissionInfo() {
        return missionInfo;
    }

    public void setMissionInfo(MissionInfo missionInfo) {
        this.missionInfo = missionInfo;
    }

    public String getTimeRequesting() {
        return timeRequesting;
    }

    public void setTimeRequesting(String timeRequesting) {
        this.timeRequesting = timeRequesting;
    }

    public String getTimeStarting() {
        return timeStarting;
    }

    public void setTimeStarting(String timeStarting) {
        this.timeStarting = timeStarting;
    }
}
