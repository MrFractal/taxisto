package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by max on 06.02.14.
 */
public class RatingItem {
    private long driverId;
    private String firstName;
    private String lastName;
    private double driverRating;
    private long position;
    private long totalPositions;

    public RatingItem() {
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public double getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(double driverRating) {
        this.driverRating = driverRating;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public long getTotalPositions() {
        return totalPositions;
    }

    public void setTotalPositions(long totalPositions) {
        this.totalPositions = totalPositions;
    }
}
