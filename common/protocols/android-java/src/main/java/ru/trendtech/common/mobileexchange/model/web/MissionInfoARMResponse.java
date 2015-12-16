package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.MissionInfo;
import ru.trendtech.common.mobileexchange.model.common.MissionInfoARM;

/**
 * Created by petr on 15.01.2015.
 */
public class MissionInfoARMResponse {
    private MissionInfoARM missionInfoARM;

    public MissionInfoARM getMissionInfoARM() {
        return missionInfoARM;
    }

    public void setMissionInfoARM(MissionInfoARM missionInfoARM) {
        this.missionInfoARM = missionInfoARM;
    }
}
