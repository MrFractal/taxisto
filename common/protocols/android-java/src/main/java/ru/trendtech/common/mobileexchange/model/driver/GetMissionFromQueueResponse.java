package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.MissionInfo;

/**
 * Created by petr on 01.04.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetMissionFromQueueResponse extends ErrorCodeHelper {
    private MissionInfo missionInfo;

    public MissionInfo getMissionInfo() {
        return missionInfo;
    }

    public void setMissionInfo(MissionInfo missionInfo) {
        this.missionInfo = missionInfo;
    }
}
