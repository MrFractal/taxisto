package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 22.10.2014.
 */

public class TerminalConfigurationRequest {
    private long missionId;
    private long terminalId;

    public long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(long terminalId) {
        this.terminalId = terminalId;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }
}
