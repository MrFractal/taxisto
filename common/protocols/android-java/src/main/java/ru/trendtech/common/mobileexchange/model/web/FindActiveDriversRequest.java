package ru.trendtech.common.mobileexchange.model.web;

/**
 * File created by max on 09/06/2014 23:28.
 */


public class FindActiveDriversRequest {
    private String city;
    private boolean showOccupied = true;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isShowOccupied() {
        return showOccupied;
    }

    public void setShowOccupied(boolean showOccupied) {
        this.showOccupied = showOccupied;
    }
}
