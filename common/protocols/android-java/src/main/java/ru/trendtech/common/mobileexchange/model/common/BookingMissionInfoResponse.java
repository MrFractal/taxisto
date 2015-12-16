package ru.trendtech.common.mobileexchange.model.common;

public class BookingMissionInfoResponse {
    private MissionInfo missionInfo;
    private DriverInfo driverInfo;

    public MissionInfo getMissionInfo() {
        return missionInfo;
    }

    public void setMissionInfo(MissionInfo missionInfo) {
        this.missionInfo = missionInfo;
    }

    public DriverInfo getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(DriverInfo driverInfo) {
        this.driverInfo = driverInfo;
    }
}
