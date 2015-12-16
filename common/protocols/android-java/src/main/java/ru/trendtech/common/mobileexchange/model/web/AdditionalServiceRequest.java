package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 07.07.2015.
 */
public class AdditionalServiceRequest {
    private String security_token;
    private long serviceId;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }
}
