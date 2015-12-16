package ru.trendtech.common.mobileexchange.model.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 11.06.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientSystemConfigurationRequest {
    private long clientId;
    private String security_token;

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }
}
