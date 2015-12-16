package ru.trendtech.common.mobileexchange.model.courier.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.courier.ItemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 25.08.2015.
 */
public class UpdateItemResponse extends ErrorCodeHelper {
    private List<ItemInfo> itemInfoList = new ArrayList<ItemInfo>();

    public List<ItemInfo> getItemInfoList() {
        return itemInfoList;
    }

    public void setItemInfoList(List<ItemInfo> itemInfoList) {
        this.itemInfoList = itemInfoList;
    }
}
