package ru.trendtech.common.mobileexchange.model.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 29.09.2014.
 */
public class TripsHistorySiteResponse {
    private List<MissionInfo> bookedAndHistory = new ArrayList<MissionInfo>();
    private long lastPageNumber;

    public long getLastPageNumber() {
        return lastPageNumber;
    }

    public void setLastPageNumber(long lastPageNumber) {
        this.lastPageNumber = lastPageNumber;
    }

    public List<MissionInfo> getBookedAndHistory() {
        return bookedAndHistory;
    }

    public void setBookedAndHistory(List<MissionInfo> bookedAndHistory) {
        this.bookedAndHistory = bookedAndHistory;
    }
}
