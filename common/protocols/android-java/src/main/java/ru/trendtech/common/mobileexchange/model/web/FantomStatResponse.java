package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 09.06.2015.
 */
public class FantomStatResponse extends ErrorCodeHelper {
    private List<FantomStatInfo> fantomStatInfos = new ArrayList<FantomStatInfo>();
    private int generalSorrySum = 0;
    private int generalMissionSum = 0;
    private int generalIncreaseSum = 0;
    private Long totalItems;
    private long lastPageNumber;

    public int getGeneralSorrySum() {
        return generalSorrySum;
    }

    public void setGeneralSorrySum(int generalSorrySum) {
        this.generalSorrySum = generalSorrySum;
    }

    public int getGeneralMissionSum() {
        return generalMissionSum;
    }

    public void setGeneralMissionSum(int generalMissionSum) {
        this.generalMissionSum = generalMissionSum;
    }

    public int getGeneralIncreaseSum() {
        return generalIncreaseSum;
    }

    public void setGeneralIncreaseSum(int generalIncreaseSum) {
        this.generalIncreaseSum = generalIncreaseSum;
    }

    public Long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Long totalItems) {
        this.totalItems = totalItems;
    }

    public long getLastPageNumber() {
        return lastPageNumber;
    }

    public void setLastPageNumber(long lastPageNumber) {
        this.lastPageNumber = lastPageNumber;
    }

    public List<FantomStatInfo> getFantomStatInfos() {
        return fantomStatInfos;
    }

    public void setFantomStatInfos(List<FantomStatInfo> fantomStatInfos) {
        this.fantomStatInfos = fantomStatInfos;
    }
}
