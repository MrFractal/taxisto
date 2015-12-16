package ru.trendtech.common.mobileexchange.model.courier.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.courier.ItemPriceInfo;
import ru.trendtech.common.mobileexchange.model.courier.OrderItemPriceInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 01.09.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItemPriceHistoryResponse extends ErrorCodeHelper {
    private List<OrderItemPriceInfo> orderItemPriceInfos = new ArrayList<OrderItemPriceInfo>();

    public List<OrderItemPriceInfo> getOrderItemPriceInfos() {
        return orderItemPriceInfos;
    }

    public void setOrderItemPriceInfos(List<OrderItemPriceInfo> orderItemPriceInfos) {
        this.orderItemPriceInfos = orderItemPriceInfos;
    }
}
