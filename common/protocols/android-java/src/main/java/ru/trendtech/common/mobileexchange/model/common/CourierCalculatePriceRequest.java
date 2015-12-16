package ru.trendtech.common.mobileexchange.model.common;

import ru.trendtech.common.mobileexchange.model.courier.OrderInfo;
import ru.trendtech.common.mobileexchange.model.courier.web.CommonRequest;

/**
 * Created by petr on 31.08.2015.
 */
public class CourierCalculatePriceRequest extends CommonRequest {
    private OrderInfo orderInfo;

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
}
