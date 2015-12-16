package ru.trendtech.common.mobileexchange.model.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.ItemLocation;
import ru.trendtech.common.mobileexchange.model.common.MissionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 08.05.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentStateMissionResponse extends ErrorCodeHelper {
    private List<ItemLocation> driverCurrentLocations = new ArrayList<ItemLocation>();
    private MissionInfo missionInfo;
    private boolean isLate;
    private boolean multipleEmpty = false;

    public boolean isMultipleEmpty() {
        return multipleEmpty;
    }

    public void setMultipleEmpty(boolean multipleEmpty) {
        this.multipleEmpty = multipleEmpty;
    }

    public List<ItemLocation> getDriverCurrentLocations() {
        return driverCurrentLocations;
    }

    public void setDriverCurrentLocations(List<ItemLocation> driverCurrentLocations) {
        this.driverCurrentLocations = driverCurrentLocations;
    }

    public MissionInfo getMissionInfo() {
        return missionInfo;
    }

    public void setMissionInfo(MissionInfo missionInfo) {
        this.missionInfo = missionInfo;
    }

    public boolean isLate() {
        return isLate;
    }

    public void setIsLate(boolean isLate) {
        this.isLate = isLate;
    }
}
