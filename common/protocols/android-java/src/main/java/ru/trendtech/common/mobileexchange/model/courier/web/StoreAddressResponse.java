package ru.trendtech.common.mobileexchange.model.courier.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.courier.StoreAddressInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 26.08.2015.
 */
public class StoreAddressResponse extends ErrorCodeHelper {
    private List<StoreAddressInfo> storeAddressInfos = new ArrayList<StoreAddressInfo>();
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

    public List<StoreAddressInfo> getStoreAddressInfos() {
        return storeAddressInfos;
    }

    public void setStoreAddressInfos(List<StoreAddressInfo> storeAddressInfos) {
        this.storeAddressInfos = storeAddressInfos;
    }
}
