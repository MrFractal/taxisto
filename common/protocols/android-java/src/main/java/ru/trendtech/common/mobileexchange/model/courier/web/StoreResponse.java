package ru.trendtech.common.mobileexchange.model.courier.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.courier.StoreInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 25.08.2015.
 */
public class StoreResponse extends ErrorCodeHelper {
   private List<StoreInfo> storeInfoList = new ArrayList<StoreInfo>();
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

    public List<StoreInfo> getStoreInfoList() {
        return storeInfoList;
    }

    public void setStoreInfoList(List<StoreInfo> storeInfoList) {
        this.storeInfoList = storeInfoList;
    }
}
