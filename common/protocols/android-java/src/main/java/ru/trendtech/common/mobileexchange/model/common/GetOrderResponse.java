package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.courier.OrderInfo;

/**
 * Created by petr on 31.08.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetOrderResponse extends ErrorCodeHelper {
    private OrderInfo orderInfo;

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
}
