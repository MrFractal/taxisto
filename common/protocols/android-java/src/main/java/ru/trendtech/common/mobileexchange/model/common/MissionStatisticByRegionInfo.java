package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 16.06.2015.
 */
public class MissionStatisticByRegionInfo {
    private RegionInfo regionInfo;
    private int countCanceledMission = 0;
    private int countCompletedMission = 0;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public RegionInfo getRegionInfo() {
        return regionInfo;
    }

    public void setRegionInfo(RegionInfo regionInfo) {
        this.regionInfo = regionInfo;
    }


    public int getCountCanceledMission() {
        return countCanceledMission;
    }

    public void setCountCanceledMission(int countCanceledMission) {
        this.countCanceledMission = countCanceledMission;
    }

    public int getCountCompletedMission() {
        return countCompletedMission;
    }

    public void setCountCompletedMission(int countCompletedMission) {
        this.countCompletedMission = countCompletedMission;
    }
}
