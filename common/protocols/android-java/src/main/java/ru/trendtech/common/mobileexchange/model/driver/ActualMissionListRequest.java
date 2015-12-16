package ru.trendtech.common.mobileexchange.model.driver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 18.09.2014.
 */


public class ActualMissionListRequest {
   private boolean mark;
   private List<Long> dirtyMissionIdList = new ArrayList<Long>();


    public List<Long> getDirtyMissionIdList() {
        return dirtyMissionIdList;
    }

    public void setDirtyMissionIdList(List<Long> dirtyMissionIdList) {
        this.dirtyMissionIdList = dirtyMissionIdList;
    }

    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }
}
