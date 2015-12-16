package ru.trendtech.common.mobileexchange.model.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.courier.web.CommonRequest;

/**
 * Created by petr on 21.04.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientMoneyAmountRequest extends CommonRequest{
}
