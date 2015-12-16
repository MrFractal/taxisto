package ru.trendtech.common.mobileexchange.model.client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 03.10.2014.
 */
public class CalculateDistanceRequest {
    // pld way: from address to lat/lon private List<String> addressList = new ArrayList<String>();
    private List<String> latLonList = new ArrayList<String>();
    private String security_token;
    private long clientId;


    public List<String> getLatLonList() {
        return latLonList;
    }

    public void setLatLonList(List<String> latLonList) {
        this.latLonList = latLonList;
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
