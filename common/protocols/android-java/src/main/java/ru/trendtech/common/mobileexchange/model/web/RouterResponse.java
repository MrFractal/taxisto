package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.RouterInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 30.06.2015.
 */
public class RouterResponse extends ErrorCodeHelper {
    private List<RouterInfo> routerInfoList = new ArrayList<RouterInfo>();
    private long lastPageNumber;
    private long totalItems;

    public List<RouterInfo> getRouterInfoList() {
        return routerInfoList;
    }

    public void setRouterInfoList(List<RouterInfo> routerInfoList) {
        this.routerInfoList = routerInfoList;
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
