package ru.trendtech.common.mobileexchange.model.courier.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.courier.web.CommonRequest;

/**
 * Created by petr on 04.09.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemHistoryRequest extends CommonRequest {
}
