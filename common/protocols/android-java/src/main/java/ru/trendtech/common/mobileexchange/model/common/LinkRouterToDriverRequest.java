package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 30.06.2015.
 */
public class LinkRouterToDriverRequest {
    private String security_token;
    private long driverId;
    private long routerId;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public long getRouterId() {
        return routerId;
    }

    public void setRouterId(long routerId) {
        this.routerId = routerId;
    }
}
