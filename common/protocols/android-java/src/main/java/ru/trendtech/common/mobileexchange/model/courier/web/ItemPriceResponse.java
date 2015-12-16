package ru.trendtech.common.mobileexchange.model.courier.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.courier.ItemPriceInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 25.08.2015.
 */
public class ItemPriceResponse extends ErrorCodeHelper {
    private List<ItemPriceInfo> itemPriceInfoList = new ArrayList<ItemPriceInfo>();
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

    public List<ItemPriceInfo> getItemPriceInfoList() {
        return itemPriceInfoList;
    }

    public void setItemPriceInfoList(List<ItemPriceInfo> itemPriceInfoList) {
        this.itemPriceInfoList = itemPriceInfoList;
    }
}
