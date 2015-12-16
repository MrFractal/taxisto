package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 27.04.2015.
 */
public class PeriodWorkResponse extends ErrorCodeHelper {
    private List<PeriodWorkInfo> periodWorkInfoList = new ArrayList<PeriodWorkInfo>();
    private long lastPageNumber;
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

    public List<PeriodWorkInfo> getPeriodWorkInfoList() {
        return periodWorkInfoList;
    }

    public void setPeriodWorkInfoList(List<PeriodWorkInfo> periodWorkInfoList) {
        this.periodWorkInfoList = periodWorkInfoList;
    }
}
