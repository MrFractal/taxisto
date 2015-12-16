package ru.trendtech.common.mobileexchange.model.common;

import java.util.List;

/**
 * Created by petr on 13.08.14.
 */
public class MissionsListResponse {
    private long lastPageNumber;
    private List<MissionInfoARM> missionInfoARM;
    private long totalItems;

    public long getLastPageNumber() {
        return lastPageNumber;
    }

    public void setLastPageNumber(long lastPageNumber) {
        this.lastPageNumber = lastPageNumber;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public List<MissionInfoARM> getMissionInfoARM() {
        return missionInfoARM;
    }

    public void setMissionInfoARM(List<MissionInfoARM> missionInfoARM) {
        this.missionInfoARM = missionInfoARM;
    }
}

