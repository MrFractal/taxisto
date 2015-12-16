package ru.trendtech.common.mobileexchange.model.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 07.07.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateAdditionalServiceResponse extends ErrorCodeHelper {
}
