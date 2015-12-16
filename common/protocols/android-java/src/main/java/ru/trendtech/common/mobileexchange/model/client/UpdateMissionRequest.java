package ru.trendtech.common.mobileexchange.model.client;

import ru.trendtech.common.mobileexchange.model.common.MissionInfo;
import ru.trendtech.common.mobileexchange.model.common.MissionInfoARM;

/**
 * Created by petr on 29.08.14.
 */

public class UpdateMissionRequest {
    private MissionInfoARM missionInfoARM;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public MissionInfoARM getMissionInfoARM() {
        return missionInfoARM;
    }

    public void setMissionInfoARM(MissionInfoARM missionInfoARM) {
        this.missionInfoARM = missionInfoARM;
    }
}
