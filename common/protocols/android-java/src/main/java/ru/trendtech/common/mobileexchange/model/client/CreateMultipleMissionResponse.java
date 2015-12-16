package ru.trendtech.common.mobileexchange.model.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.MissionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 29.04.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateMultipleMissionResponse extends ErrorCodeHelper {
    private List<MissionInfo> missionInfos = new ArrayList<MissionInfo>();

    public List<MissionInfo> getMissionInfos() {
        return missionInfos;
    }

    public void setMissionInfos(List<MissionInfo> missionInfos) {
        this.missionInfos = missionInfos;
    }
}
