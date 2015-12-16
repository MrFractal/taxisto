package ru.trendtech.common.mobileexchange.model.courier;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 21.08.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemInfo {
    private long id;
    private String itemName;
    private String itemType; // PRODUCT, SERVICE
    private boolean active; // 0 - какой-то товар мы больше не возим (мясо, например)
    private int defaultItemPrice;
    private List<ItemPropertyInfo> itemPropertyInfos = new ArrayList<ItemPropertyInfo>();


    public List<ItemPropertyInfo> getItemPropertyInfos() {
        return itemPropertyInfos;
    }

    public void setItemPropertyInfos(List<ItemPropertyInfo> itemPropertyInfos) {
        this.itemPropertyInfos = itemPropertyInfos;
    }

    public int getDefaultItemPrice() {
        return defaultItemPrice;
    }

    public void setDefaultItemPrice(int defaultItemPrice) {
        this.defaultItemPrice = defaultItemPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
