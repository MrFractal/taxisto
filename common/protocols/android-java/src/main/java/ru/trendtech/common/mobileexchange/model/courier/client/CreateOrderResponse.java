package ru.trendtech.common.mobileexchange.model.courier.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.courier.CustomWindowInfo;

/**
 * Created by petr on 25.08.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateOrderResponse extends ErrorCodeHelper {
    private CustomWindowInfo customWindowInfo;
    private long orderId = 0;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public CustomWindowInfo getCustomWindowInfo() {
        return customWindowInfo;
    }

    public void setCustomWindowInfo(CustomWindowInfo customWindowInfo) {
        this.customWindowInfo = customWindowInfo;
    }
}
