package ru.trendtech.common.mobileexchange.model.driver;

/**
 * File created by max on 06/05/2014 22:43.
 */
public class WifiAvailableRequest {
    private long missionId;
    private String networkId;
    private String password;

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
