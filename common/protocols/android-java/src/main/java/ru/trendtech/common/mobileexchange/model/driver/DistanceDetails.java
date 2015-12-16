package ru.trendtech.common.mobileexchange.model.driver;

import ru.trendtech.common.mobileexchange.model.common.MissionInfo;

/**
 * Created by petr on 02.04.2015.
 */
public class DistanceDetails {
    private MissionInfo missionInfo;
    private double distanceToClient;

    public MissionInfo getMissionInfo() {
        return missionInfo;
    }

    public void setMissionInfo(MissionInfo missionInfo) {
        this.missionInfo = missionInfo;
    }

    public double getDistanceToClient() {
        return distanceToClient;
    }

    public void setDistanceToClient(double distanceToClient) {
        this.distanceToClient = distanceToClient;
    }
}
