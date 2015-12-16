package ru.trendtech.common.mobileexchange.model.client;

import ru.trendtech.common.mobileexchange.model.common.DriverRequest;

/**
 * Created by max on 06.02.14.
 */
public class AssignedDriverLocationRequest extends DriverRequest {
    private long driverId;

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }
}
