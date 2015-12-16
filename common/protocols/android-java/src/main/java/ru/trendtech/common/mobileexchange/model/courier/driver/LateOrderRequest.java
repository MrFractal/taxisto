package ru.trendtech.common.mobileexchange.model.courier.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.courier.web.CommonRequest;

/**
 * Created by petr on 15.09.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LateOrderRequest extends CommonRequest{
    private long orderId;
    private int minutesLate;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getMinutesLate() {
        return minutesLate;
    }

    public void setMinutesLate(int minutesLate) {
        this.minutesLate = minutesLate;
    }
}
