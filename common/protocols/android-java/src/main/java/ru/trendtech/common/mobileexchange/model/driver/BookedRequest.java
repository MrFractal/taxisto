package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.DriverRequest;

/**
 * File created by max on 08/05/2014 11:08.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookedRequest extends DriverRequest {
    public BookedRequest() {
    }

    public BookedRequest(long driverId) {
        super(driverId);
    }
}
