package ru.trendtech.common.mobileexchange.model.courier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 21.08.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemPriceInfo {
    private Long id;
    private ItemInfo itemInfo;
    private StoreAddressInfo storeAddressInfo; // если это услуга, то магазина может и не быть
    private int price;
    private long timeOfFinishPricing;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ItemInfo getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(ItemInfo itemInfo) {
        this.itemInfo = itemInfo;
    }

    public StoreAddressInfo getStoreAddressInfo() {
        return storeAddressInfo;
    }

    public void setStoreAddressInfo(StoreAddressInfo storeAddressInfo) {
        this.storeAddressInfo = storeAddressInfo;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getTimeOfFinishPricing() {
        return timeOfFinishPricing;
    }

    public void setTimeOfFinishPricing(long timeOfFinishPricing) {
        this.timeOfFinishPricing = timeOfFinishPricing;
    }
}
