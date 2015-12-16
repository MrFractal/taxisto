package ru.trendtech.common.mobileexchange.model.driver;

import ru.trendtech.common.mobileexchange.model.common.DriverRequest;

/**
 * Created by petr on 10.02.2015.
 */
public class AlarmRequest extends DriverRequest {
    private boolean start = true;

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }
}
