package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 10.02.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlarmResponse extends ErrorCodeHelper {
}
