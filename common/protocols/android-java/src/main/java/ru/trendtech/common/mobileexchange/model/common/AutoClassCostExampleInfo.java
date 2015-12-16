package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 19.06.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AutoClassCostExampleInfo {
    private String addressFrom;
    private String addressTo;
    private String cost;

    public String getAddressFrom() {
        return addressFrom;
    }

    public void setAddressFrom(String addressFrom) {
        this.addressFrom = addressFrom;
    }

    public String getAddressTo() {
        return addressTo;
    }

    public void setAddressTo(String addressTo) {
        this.addressTo = addressTo;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
