package ru.trendtech.common.mobileexchange.model.web.corporate;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.corporate.MissionInfoCorporate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 28.04.2015.
 */
public class CorporateMissionStatResponse extends ErrorCodeHelper{
    private long lastPageNumber;
    private Long totalItems;
    private List<MissionInfoCorporate> missionInfos = new ArrayList<MissionInfoCorporate>();
    private int sum;
    private int allSum;

    public long getLastPageNumber() {
        return lastPageNumber;
    }

    public void setLastPageNumber(long lastPageNumber) {
        this.lastPageNumber = lastPageNumber;
    }

    public Long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Long totalItems) {
        this.totalItems = totalItems;
    }

    public List<MissionInfoCorporate> getMissionInfos() {
        return missionInfos;
    }

    public void setMissionInfos(List<MissionInfoCorporate> missionInfos) {
        this.missionInfos = missionInfos;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getAllSum() {
        return allSum;
    }

    public void setAllSum(int allSum) {
        this.allSum = allSum;
    }
}
