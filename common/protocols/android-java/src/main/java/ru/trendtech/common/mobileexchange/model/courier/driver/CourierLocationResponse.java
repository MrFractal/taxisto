package ru.trendtech.common.mobileexchange.model.courier.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 21.09.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourierLocationResponse extends ErrorCodeHelper {
}
