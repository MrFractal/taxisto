package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 25.08.14.
 */

public class PayTerminalLocation {
    private long driverId;
    private double la = 0;
    private double lo = 0;
    private String city;
    private String street;
    private String house;



    public String getFullAddress(){
        return getCity()+" "+getStreet()+" "+getHouse();
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

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public double getLa() {
        return la;
    }

    public void setLa(double la) {
        this.la = la;
    }

    public double getLo() {
        return lo;
    }

    public void setLo(double lo) {
        this.lo = lo;
    }
}
