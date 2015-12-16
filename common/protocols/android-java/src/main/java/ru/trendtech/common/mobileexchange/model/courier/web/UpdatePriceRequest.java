package ru.trendtech.common.mobileexchange.model.courier.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 20.10.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdatePriceRequest extends CommonRequest {
    private long orderId;
    private int priceItemsInFact;

    public int getPriceItemsInFact() {
        return priceItemsInFact;
    }

    public void setPriceItemsInFact(int priceItemsInFact) {
        this.priceItemsInFact = priceItemsInFact;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
