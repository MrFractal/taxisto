package ru.trendtech.common.mobileexchange.model.courier.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 25.08.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemPriceRequest extends CommonRequest {
    private long storeId;
    private long itemId;
    private long orderId;
    private int numberPage;
    private int sizePage;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public int getNumberPage() {
        return numberPage;
    }

    public void setNumberPage(int numberPage) {
        this.numberPage = numberPage;
    }

    public int getSizePage() {
        return sizePage;
    }

    public void setSizePage(int sizePage) {
        this.sizePage = sizePage;
    }
}
