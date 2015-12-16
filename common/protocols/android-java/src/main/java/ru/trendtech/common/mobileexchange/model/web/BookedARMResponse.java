package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.MissionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 14.01.2015.
 */
public class BookedARMResponse {
    private int bookedNew = 0;
    private int bookedToMe = 0;
    private List<MissionInfo> missions = new ArrayList<MissionInfo>();

    public int getBookedNew() {
        return bookedNew;
    }

    public void setBookedNew(int bookedNew) {
        this.bookedNew = bookedNew;
    }

    public int getBookedToMe() {
        return bookedToMe;
    }

    public void setBookedToMe(int bookedToMe) {
        this.bookedToMe = bookedToMe;
    }

    public List<MissionInfo> getMissions() {
        return missions;
    }

    public void setMissions(List<MissionInfo> missions) {
        this.missions = missions;
    }
}
