package ru.trendtech.common.mobileexchange.model.courier.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.courier.OrderInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 01.09.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderHistoryResponse extends ErrorCodeHelper {
    private List<OrderInfo> orderInfos =  new ArrayList<OrderInfo>();

    public List<OrderInfo> getOrderInfos() {
        return orderInfos;
    }

    public void setOrderInfos(List<OrderInfo> orderInfos) {
        this.orderInfos = orderInfos;
    }
}
