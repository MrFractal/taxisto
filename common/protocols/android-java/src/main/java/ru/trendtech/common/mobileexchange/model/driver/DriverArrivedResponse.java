package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.DumbResponse;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by max on 09.02.14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverArrivedResponse  extends ErrorCodeHelper {
}
