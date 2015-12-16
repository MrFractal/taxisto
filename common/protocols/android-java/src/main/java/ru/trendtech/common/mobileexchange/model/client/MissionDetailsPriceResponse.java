package ru.trendtech.common.mobileexchange.model.client;

import java.util.List;

public class MissionDetailsPriceResponse {
    private String totalPrice;
    private List<MissionDetailsInfo> missionDetailsInfo;

    public String getPrice() { return totalPrice; }

    public void setPrice(String totalPrice) { this.totalPrice = totalPrice; }

    public List<MissionDetailsInfo> getMissionDetailsInfo() {
        return missionDetailsInfo;
    }

    public void setMissionDetailsInfo(List<MissionDetailsInfo> missionDetailsInfo) {
        this.missionDetailsInfo = missionDetailsInfo;
    }
}
