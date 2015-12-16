package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by max on 06.02.14.
 */
public class DriversAroundRequest {
    private ItemLocation currentLocation;
    private int radius;

    public ItemLocation getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(ItemLocation currentLocation) {
        this.currentLocation = currentLocation;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
