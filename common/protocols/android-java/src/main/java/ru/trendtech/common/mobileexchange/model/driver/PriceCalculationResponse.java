package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.DumbResponse;
import ru.trendtech.common.mobileexchange.model.common.rates.PaymentInfo;

/**
 * Created by max on 13.02.14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceCalculationResponse  extends DumbResponse {
    private PaymentInfo paymentInfo;

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }
}
