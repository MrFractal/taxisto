package ru.trendtech.common.mobileexchange.model.client;

import ru.trendtech.common.mobileexchange.model.common.MissionInfo;

/**
 * Created by petr on 29.10.2014.
 */
public class FreeDriverRequestSTR {
    private MissionInfo missionInfo;
    private String timeOfStarting;
    private String security_token;


    public String getTimeOfStarting() {
        return timeOfStarting;
    }

    public void setTimeOfStarting(String timeOfStarting) {
        this.timeOfStarting = timeOfStarting;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public MissionInfo getMissionInfo() {
        return missionInfo;
    }

    public void setMissionInfo(MissionInfo missionInfo) {
        this.missionInfo = missionInfo;
    }
}
