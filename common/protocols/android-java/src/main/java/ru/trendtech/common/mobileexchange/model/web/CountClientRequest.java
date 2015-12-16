package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 07.11.2014.
 */
public class CountClientRequest {
    private long timeRegistrationStart;
    private long timeRegistrationEnd;

    public long getTimeRegistrationStart() {
        return timeRegistrationStart;
    }

    public void setTimeRegistrationStart(long timeRegistrationStart) {
        this.timeRegistrationStart = timeRegistrationStart;
    }

    public long getTimeRegistrationEnd() {
        return timeRegistrationEnd;
    }

    public void setTimeRegistrationEnd(long timeRegistrationEnd) {
        this.timeRegistrationEnd = timeRegistrationEnd;
    }
}
