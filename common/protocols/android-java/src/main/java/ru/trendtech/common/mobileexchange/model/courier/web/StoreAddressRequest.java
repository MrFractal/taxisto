package ru.trendtech.common.mobileexchange.model.courier.web;

import ru.trendtech.common.mobileexchange.model.courier.CommentInfo;
import ru.trendtech.common.mobileexchange.model.courier.StoreAddressInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 26.08.2015.
 */
public class StoreAddressRequest extends CommonRequest {
    private long orderId;
    private long storeAddressId;
    private long storeId;
    private int numberPage;
    private int sizePage;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getStoreAddressId() {
        return storeAddressId;
    }

    public void setStoreAddressId(long storeAddressId) {
        this.storeAddressId = storeAddressId;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
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
