package ru.trendtech.common.mobileexchange.model.courier.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.courier.ItemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 25.08.2015.
 */
public class ItemResponse extends ErrorCodeHelper {
    private List<ItemInfo> itemInfos = new ArrayList<ItemInfo>();
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

    public List<ItemInfo> getItemInfos() {
        return itemInfos;
    }

    public void setItemInfos(List<ItemInfo> itemInfos) {
        this.itemInfos = itemInfos;
    }
}
