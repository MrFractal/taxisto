package ru.trendtech.common.mobileexchange.model.courier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * Created by petr on 21.08.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderAddressInfo {
    private long id;
    private String address;
    private double latitude;
    private double longitude;
    private String city;
    private String region;
    private String street;
    private String house;
    private String housing;
    private String apartment;
    private String contactPerson;
    private String contactPersonPhone;
    private String comment;
    @Deprecated
    private boolean isToAddress;
    private boolean to;
    private int targetAddressState; // UNKNOWN(0), CURRENT_ADDRESS(1), TOOK(2), DELIVERED(3), PROBLEM(4)

    public boolean isTo() {
        return to;
    }

    public void setTo(boolean to) {
        this.to = to;
    }

    public int getTargetAddressState() {
        return targetAddressState;
    }

    public void setTargetAddressState(int targetAddressState) {
        this.targetAddressState = targetAddressState;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getHousing() {
        return housing;
    }

    public void setHousing(String housing) {
        this.housing = housing;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public boolean isToAddress() {
        return this.isToAddress;
    }

    public void setIsToAddress(boolean isToAddress) {
        this.isToAddress = isToAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }
}
