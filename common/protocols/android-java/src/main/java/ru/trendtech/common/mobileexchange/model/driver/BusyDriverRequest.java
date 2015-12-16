package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.DriverRequest;

/**
 * Created by max on 09.02.14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusyDriverRequest extends DriverRequest {
    private boolean value = true;

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
