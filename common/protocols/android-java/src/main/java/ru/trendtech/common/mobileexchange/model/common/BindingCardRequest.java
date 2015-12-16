package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.courier.web.CommonRequest;

/**
 * Created by petr on 24.09.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BindingCardRequest extends CommonRequest {
}
