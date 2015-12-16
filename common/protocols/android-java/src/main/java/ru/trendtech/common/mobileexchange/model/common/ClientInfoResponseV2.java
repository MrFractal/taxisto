package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 09.06.2015.
 */
public class ClientInfoResponseV2 extends ErrorCodeHelper {
    private ClientInfo clientInfo;
    private int missionCount;

    public int getMissionCount() {
        return missionCount;
    }

    public void setMissionCount(int missionCount) {
        this.missionCount = missionCount;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }
}
