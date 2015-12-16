package ru.trendtech.common.mobileexchange.model.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 24.11.2014.
 */
public class TripsHistorySTRResponse {
    private List<MissionHistory> history = new ArrayList();
    private List<MissionHistory> booked = new ArrayList();

    public List<MissionHistory> getHistory() {
        return history;
    }

    public void setHistory(List<MissionHistory> history) {
        this.history = history;
    }

    public List<MissionHistory> getBooked() {
        return booked;
    }

    public void setBooked(List<MissionHistory> booked) {
        this.booked = booked;
    }
}
