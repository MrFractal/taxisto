package ru.trendtech.common.mobileexchange.model.courier.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 20.10.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdatePriceResponse extends ErrorCodeHelper {
}
