package ru.trendtech.common.mobileexchange.model.driver;


import ru.trendtech.common.mobileexchange.model.courier.web.CommonRequest;

public class NearestTerminalsRequest extends CommonRequest{
    private long driverId;


    public NearestTerminalsRequest(long driverId) {
        this.driverId = driverId;
    }

    public NearestTerminalsRequest(){}


    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public long getDriverId() {
        return driverId;
    }
}
