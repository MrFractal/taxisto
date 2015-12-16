package ru.trendtech.common.mobileexchange.model.common.billing.paymobile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 06.02.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AskClientForPaymentCardResponse extends ErrorCodeHelper{
}
