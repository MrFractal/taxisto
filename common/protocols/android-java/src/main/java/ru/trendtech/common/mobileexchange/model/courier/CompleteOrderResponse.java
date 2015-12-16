package ru.trendtech.common.mobileexchange.model.courier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 07.10.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompleteOrderResponse extends ErrorCodeHelper {
    
}
