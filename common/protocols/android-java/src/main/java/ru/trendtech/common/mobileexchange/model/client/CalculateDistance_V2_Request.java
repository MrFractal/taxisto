package ru.trendtech.common.mobileexchange.model.client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 10.11.2014.
 */
public class CalculateDistance_V2_Request {
    private List<String> addressList = new ArrayList<String>();
    private String security_token;
    private long clientId;


    public List<String> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<String> addressList) {
        this.addressList = addressList;
    }

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
