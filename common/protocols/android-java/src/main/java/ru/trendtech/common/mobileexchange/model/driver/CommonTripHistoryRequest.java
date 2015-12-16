package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.courier.web.CommonRequest;

/**
 * Created by petr on 27.10.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonTripHistoryRequest extends CommonRequest{
}
