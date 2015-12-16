package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 06.02.14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TripsHistoryResponse {
    private List<MissionInfo> history = new ArrayList<MissionInfo>();

    private List<MissionInfo> booked = new ArrayList<MissionInfo>();

    public List<MissionInfo> getHistory() {
        return history;
    }

    public List<MissionInfo> getBooked() {
        return booked;
    }

    public void setHistory(List<MissionInfo> history) {
        this.history = history;
    }

    public void setBooked(List<MissionInfo> booked) {
        this.booked = booked;
    }
}
