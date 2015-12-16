package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by max on 06.02.14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssignMissionResponse {
    private boolean assigned = false;
    private int time;
    private boolean booked = false;

    public boolean isBooked() { return booked; }

    public void setBooked(boolean booked) { this.booked = booked; }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
