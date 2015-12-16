package ru.trendtech.common.mobileexchange.model.client;

import ru.trendtech.common.mobileexchange.model.common.ClientCardInfo;

/**
 * Created by petr on 16.09.2014.
 */

public class UpdateClientCardRequest {
   private ClientCardInfo clientCardInfo;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public ClientCardInfo getClientCardInfo() {
        return clientCardInfo;
    }

    public void setClientCardInfo(ClientCardInfo clientCardInfo) {
        this.clientCardInfo = clientCardInfo;
    }
}
