package ru.trendtech.common.mobileexchange.model.client;

import ru.trendtech.common.mobileexchange.model.common.MissionInfo;

/**
 * Created by petr on 22.10.2014.
 */
public class FreeDriverTerminalRequest {
    private MissionInfo missionInfo;
    private String security_token;
    private long terminalId;

    public MissionInfo getMissionInfo() {
        return missionInfo;
    }

    public void setMissionInfo(MissionInfo missionInfo) {
        this.missionInfo = missionInfo;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(long terminalId) {
        this.terminalId = terminalId;
    }
}
