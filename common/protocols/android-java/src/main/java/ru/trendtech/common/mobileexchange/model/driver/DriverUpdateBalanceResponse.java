package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 21.08.14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverUpdateBalanceResponse extends ErrorCodeHelper{

}
