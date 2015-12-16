package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 19.01.2015.
 */
public class DriverEstimatesRequest {
    private long driverId;
    private long clientId;
    private String security_token;

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }
}
