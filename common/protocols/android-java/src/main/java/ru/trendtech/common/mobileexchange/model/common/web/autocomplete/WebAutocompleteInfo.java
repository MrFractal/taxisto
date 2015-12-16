package ru.trendtech.common.mobileexchange.model.common.web.autocomplete;

/**
 * Created by petr on 27.08.2015.
 */
public class WebAutocompleteInfo {
    private String street = "";
    private String region = "";
    private String city = "";
    private String house = "";
    private String latitude = "";
    private String longitude = "";
    private double distance = 0.0;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "street: "+street+" region: "+region+" house: "+house+" latitude: "+latitude+" longitude: "+longitude;
    }
}
