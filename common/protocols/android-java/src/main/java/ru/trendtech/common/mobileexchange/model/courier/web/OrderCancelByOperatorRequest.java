package ru.trendtech.common.mobileexchange.model.courier.web;

/**
 * Created by petr on 06.10.2015.
 */
public class OrderCancelByOperatorRequest extends CommonRequest {
    private String reason;
    private long orderId;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
