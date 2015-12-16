package ru.trendtech.common.mobileexchange.model.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 25.03.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateClientARMResponse extends ErrorCodeHelper{
}
