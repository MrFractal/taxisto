package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.MissionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 18.09.2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActualMissionListResponse {
    private List<MissionInfo> actualMissionList = new ArrayList();
    private boolean mark;


    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public List<MissionInfo> getActualMissionList() {
        return actualMissionList;
    }

    public void setActualMissionList(List<MissionInfo> actualMissionList) {
        this.actualMissionList = actualMissionList;
    }
}
