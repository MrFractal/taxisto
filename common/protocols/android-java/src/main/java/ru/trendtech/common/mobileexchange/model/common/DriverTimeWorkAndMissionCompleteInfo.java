package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 19.05.2015.
 */
public class DriverTimeWorkAndMissionCompleteInfo {
    private int countMission;
    private int sumMission;
    private DriverTimeWorkInfo driverTimeWorkInfo;

    public DriverTimeWorkInfo getDriverTimeWorkInfo() {
        return driverTimeWorkInfo;
    }

    public void setDriverTimeWorkInfo(DriverTimeWorkInfo driverTimeWorkInfo) {
        this.driverTimeWorkInfo = driverTimeWorkInfo;
    }

    public int getCountMission() {
        return countMission;
    }

    public void setCountMission(int countMission) {
        this.countMission = countMission;
    }

    public int getSumMission() {
        return sumMission;
    }

    public void setSumMission(int sumMission) {
        this.sumMission = sumMission;
    }
}
