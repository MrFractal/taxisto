package ru.trendtech.common.mobileexchange.model.notifications;

/**
 * Created by ivanenok on 4/14/2014.
 */
public class DriverArrivedNotification {
    private long id;
    private int freeTime;
    private boolean booking;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(int freeTime) {
        this.freeTime = freeTime;
    }

    public boolean isBooking() {
        return booking;
    }

    public void setBooking(boolean booking) {
        this.booking = booking;
    }
}
