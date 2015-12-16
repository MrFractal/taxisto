package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 30.03.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TakeMissionDeclinedResponse extends ErrorCodeHelper {
}
