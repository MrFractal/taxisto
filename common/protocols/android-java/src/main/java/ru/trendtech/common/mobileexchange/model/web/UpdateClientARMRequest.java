package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ClientInfoARM;
import ru.trendtech.common.mobileexchange.model.common.corporate.ClientInfoCorporate;
import ru.trendtech.common.mobileexchange.model.common.corporate.ClientInfoCorporateARM;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 25.03.2015.
 */
public class UpdateClientARMRequest {
    private String security_token;
    private List<ClientInfoARM> clientInfoARMs = new ArrayList<ClientInfoARM>();

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public List<ClientInfoARM> getClientInfoARMs() {
        return clientInfoARMs;
    }

    public void setClientInfoARMs(List<ClientInfoARM> clientInfoARMs) {
        this.clientInfoARMs = clientInfoARMs;
    }
}
