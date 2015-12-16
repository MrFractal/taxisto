package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.DriverRequest;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetDeviceIdRequest extends DriverRequest {
}
