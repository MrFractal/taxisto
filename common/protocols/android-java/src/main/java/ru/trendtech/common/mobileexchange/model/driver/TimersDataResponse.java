package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 12.05.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimersDataResponse extends ErrorCodeHelper {
}
