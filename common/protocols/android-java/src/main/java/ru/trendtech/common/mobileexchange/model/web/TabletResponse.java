package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.TabletInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 22.06.2015.
 */
public class TabletResponse extends ErrorCodeHelper {
    private List<TabletInfo> tabletInfoList = new ArrayList<TabletInfo>();
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

    public List<TabletInfo> getTabletInfoList() {
        return tabletInfoList;
    }

    public void setTabletInfoList(List<TabletInfo> tabletInfoList) {
        this.tabletInfoList = tabletInfoList;
    }
}
