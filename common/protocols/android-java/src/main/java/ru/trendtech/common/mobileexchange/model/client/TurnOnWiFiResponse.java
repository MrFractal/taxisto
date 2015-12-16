package ru.trendtech.common.mobileexchange.model.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 06.07.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TurnOnWiFiResponse extends ErrorCodeHelper {
}
