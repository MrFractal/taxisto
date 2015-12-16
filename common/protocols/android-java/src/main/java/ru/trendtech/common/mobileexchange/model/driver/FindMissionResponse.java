package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.MissionInfo;

/**
 * Created by max on 06.02.14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FindMissionResponse {
    private MissionInfo missionInfo;

    public MissionInfo getMissionInfo() {
        return missionInfo;
    }

    public void setMissionInfo(MissionInfo missionInfo) {
        this.missionInfo = missionInfo;
    }

}
