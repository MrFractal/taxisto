package ru.trendtech.common.mobileexchange.model.courier.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 19.10.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThreedSecureInitializeResponse extends ErrorCodeHelper {

}
