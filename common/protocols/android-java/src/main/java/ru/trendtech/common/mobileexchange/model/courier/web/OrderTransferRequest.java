package ru.trendtech.common.mobileexchange.model.courier.web;

/**
 * Created by petr on 03.10.2015.
 */
public class OrderTransferRequest extends CommonRequest {
    private long orderId;
    private long toCourierId;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getToCourierId() {
        return toCourierId;
    }

    public void setToCourierId(long toCourierId) {
        this.toCourierId = toCourierId;
    }
}
