package ru.trendtech.models.asterisk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivanenok on 4/13/2014.
 */
public class DriverArrivingTimeResponse extends AsteriskResponse {
    private List<Integer> times = new ArrayList<>();

    public DriverArrivingTimeResponse() {
    }

    public DriverArrivingTimeResponse(AsteriskRequest request) {
        super(request);
    }

    public List<Integer> getTimes() {
        return times;
    }

    public void setTimes(List<Integer> times) {
        this.times = times;
    }
}
