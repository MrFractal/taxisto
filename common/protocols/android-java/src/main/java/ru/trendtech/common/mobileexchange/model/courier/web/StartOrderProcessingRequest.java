package ru.trendtech.common.mobileexchange.model.courier.web;

/**
 * Created by petr on 04.09.2015.
 */
public class StartOrderProcessingRequest extends CommonRequest{
    private long orderId;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
