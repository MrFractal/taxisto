package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 29.03.2015.
 */
public class CheckPointInsidePolygonRequest {
    private String security_token;
    private double latitude;
    private double longitude;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
