package ru.trendtech.common.mobileexchange.model.courier.web;

import ru.trendtech.common.mobileexchange.model.courier.ItemPriceInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 25.08.2015.
 */
public class UpdateItemPriceRequest extends CommonRequest {
    private List<ItemPriceInfo> itemPriceInfoList = new ArrayList<ItemPriceInfo>();

    public List<ItemPriceInfo> getItemPriceInfoList() {
        return itemPriceInfoList;
    }

    public void setItemPriceInfoList(List<ItemPriceInfo> itemPriceInfoList) {
        this.itemPriceInfoList = itemPriceInfoList;
    }
}
