package ru.trendtech.common.mobileexchange.model.courier.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.courier.web.CommonRequest;

/**
 * Created by petr on 17.09.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourierConfigurationRequest extends CommonRequest {
}
