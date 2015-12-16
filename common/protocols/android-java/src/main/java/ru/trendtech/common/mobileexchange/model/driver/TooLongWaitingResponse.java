package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.DumbResponse;

/**
 * Created by ivanenok on 5/9/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TooLongWaitingResponse extends DumbResponse {
}
