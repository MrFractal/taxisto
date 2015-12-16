package ru.trendtech.common.mobileexchange.model.notifications;

import ru.trendtech.common.mobileexchange.model.common.MissionInfo;

/**
 * Created by ivanenok on 4/14/2014.
 */
public class DriverRequiredNotification {
    private MissionInfo missionInfo;

    public MissionInfo getMissionInfo() {
        return missionInfo;
    }

    public void setMissionInfo(MissionInfo missionInfo) {
        this.missionInfo = missionInfo;
    }
}
