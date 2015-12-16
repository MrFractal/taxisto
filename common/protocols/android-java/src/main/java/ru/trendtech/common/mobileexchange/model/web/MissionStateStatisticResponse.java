package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.MissionStateStatisticInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 10.02.2015.
 */
public class MissionStateStatisticResponse extends ErrorCodeHelper {
    private List<MissionStateStatisticInfo> missionStateStatisticInfos = new ArrayList<MissionStateStatisticInfo>();
    private long lastPageNumber;
    private long totalItems;

    public List<MissionStateStatisticInfo> getMissionStateStatisticInfos() {
        return missionStateStatisticInfos;
    }

    public void setMissionStateStatisticInfos(List<MissionStateStatisticInfo> missionStateStatisticInfos) {
        this.missionStateStatisticInfos = missionStateStatisticInfos;
    }

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
}
