package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 28.01.2015.
 */
public class BanPeriodRestDriverInfo {
    private long id;
    private long timeOfStarting;
    private long timeOfEnding;
    private boolean active;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimeOfStarting() {
        return timeOfStarting;
    }

    public void setTimeOfStarting(long timeOfStarting) {
        this.timeOfStarting = timeOfStarting;
    }

    public long getTimeOfEnding() {
        return timeOfEnding;
    }

    public void setTimeOfEnding(long timeOfEnding) {
        this.timeOfEnding = timeOfEnding;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
