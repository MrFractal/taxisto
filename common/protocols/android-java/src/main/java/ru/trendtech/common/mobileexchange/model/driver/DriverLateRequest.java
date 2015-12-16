package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.DriverRequest;

/**
 * Created by max on 13.02.14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverLateRequest  extends DriverRequest {
    private int time;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
