package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 05.08.14.
 */

public class RatesDriverRequest {
    private long driverId;
    private String security_token;

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
}
