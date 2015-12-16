package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemLocation {
    private long a; // itemId
    private double la = 0;
    private double lo = 0;
    private boolean o = false;
    private int angle;


    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public long getDriverId() {
        return a;
    }

    public void setDriverId(long driverId) {
        this.a = driverId;
    }

    public double getLatitude() {
        return la;
    }

    public void setLatitude(double latitude) {
        this.la = latitude;
    }

    public double getLongitude() {
        return lo;
    }

    public void setLongitude(double longitude) {
        this.lo = longitude;
    }

    public boolean isOccupied() {
        return o;
    }

    public void setOccupied(boolean occupied) {
        this.o = occupied;
    }


}
