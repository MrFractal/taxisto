package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 31.03.2015.
 */
public class DriverActivityInfo {
    private long driverId;
    private String firstName;
    private String lastName;
    private String timeOnline;
    private String timeOnlineBusy;
    private String timeOffline;

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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTimeOnline() {
        return timeOnline;
    }

    public void setTimeOnline(String timeOnline) {
        this.timeOnline = timeOnline;
    }

    public String getTimeOnlineBusy() {
        return timeOnlineBusy;
    }

    public void setTimeOnlineBusy(String timeOnlineBusy) {
        this.timeOnlineBusy = timeOnlineBusy;
    }

    public String getTimeOffline() {
        return timeOffline;
    }

    public void setTimeOffline(String timeOffline) {
        this.timeOffline = timeOffline;
    }
}
