package ru.trendtech.common.mobileexchange.model.courier.web;

/**
 * Created by petr on 07.09.2015.
 */
public class UpdateOrderStateRequest extends CommonRequest {
    private long orderId;
    private String state;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

/*
        NONE,
        NEW, // новый заказ
        IN_PROGRESS_BY_OPERATOR, // взят оператором для исполнения
        WAIT_TO_CONFIRM,
        CONFIRMED, // подтвержден
        IN_PROGRESS_BY_COURIER, // взят курьером для исполнения
        COMPLETED,
        CANCELED,
 */
