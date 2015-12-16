package ru.trendtech.common.mobileexchange.model.client;

import ru.trendtech.common.mobileexchange.model.common.DriverInfo;
import ru.trendtech.common.mobileexchange.model.common.MissionInfo;
import ru.trendtech.common.mobileexchange.model.common.MissionInfoARM;

/**
 * Created by petr on 29.08.14.
 */

public class UpdateMissionResponse {
    private boolean update = false;
    private MissionInfoARM missionInfoARM;

    public MissionInfoARM getMissionInfoARM() {
        return missionInfoARM;
    }

    public void setMissionInfoARM(MissionInfoARM missionInfoARM) {
        this.missionInfoARM = missionInfoARM;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}
