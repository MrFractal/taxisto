package ru.trendtech.common.mobileexchange.model.notifications;

/**
 * Created by max on 13.02.14.
 */
public class DriverLateNotification {
    private long id;
    private int time;

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
}
