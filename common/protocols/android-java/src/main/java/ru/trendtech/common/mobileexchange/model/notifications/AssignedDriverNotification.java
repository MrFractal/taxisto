package ru.trendtech.common.mobileexchange.model.notifications;

/**
 * Created by max on 06.02.14.
 */
public class AssignedDriverNotification {
    private long id;
    private int time;
    private boolean booked = false;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }
}
