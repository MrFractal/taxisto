package ru.trendtech.common.mobileexchange.model.courier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 03.09.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientItemInfo {
    private long id;
    private ItemInfo itemInfo; /* выбрал из списка, если хочет что-нибудь уникальное - вставляет название в итем инфо без id */
    private String undefinedItemName;
    private int countItem;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ItemInfo getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(ItemInfo itemInfo) {
        this.itemInfo = itemInfo;
    }

    public String getUndefinedItemName() {
        return undefinedItemName;
    }

    public void setUndefinedItemName(String undefinedItemName) {
        this.undefinedItemName = undefinedItemName;
    }

    public int getCountItem() {
        return countItem;
    }

    public void setCountItem(int countItem) {
        this.countItem = countItem;
    }
}
