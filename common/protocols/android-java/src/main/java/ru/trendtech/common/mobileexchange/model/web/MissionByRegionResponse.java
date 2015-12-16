package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.MissionStatisticByRegionInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 16.06.2015.
 */
public class MissionByRegionResponse extends ErrorCodeHelper {
    private List<MissionStatisticByRegionInfo> missionStatisticByRegionInfos = new ArrayList<MissionStatisticByRegionInfo>();

    public List<MissionStatisticByRegionInfo> getMissionStatisticByRegionInfos() {
        return missionStatisticByRegionInfos;
    }

    public void setMissionStatisticByRegionInfos(List<MissionStatisticByRegionInfo> missionStatisticByRegionInfos) {
        this.missionStatisticByRegionInfos = missionStatisticByRegionInfos;
    }
}
