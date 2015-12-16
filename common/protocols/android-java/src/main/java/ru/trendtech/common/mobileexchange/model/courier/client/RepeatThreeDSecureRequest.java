package ru.trendtech.common.mobileexchange.model.courier.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.courier.web.CommonRequest;

/**
 * Created by petr on 01.10.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepeatThreeDSecureRequest extends CommonRequest {
    private long orderPaymentId;

    public long getOrderPaymentId() {
        return orderPaymentId;
    }

    public void setOrderPaymentId(long orderPaymentId) {
        this.orderPaymentId = orderPaymentId;
    }
}
