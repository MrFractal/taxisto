package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.MissionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * File created by max on 08/05/2014 11:09.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookedResponse{
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
