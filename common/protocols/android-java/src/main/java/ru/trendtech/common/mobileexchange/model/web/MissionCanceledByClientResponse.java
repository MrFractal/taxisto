package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 08.06.2015.
 */
public class MissionCanceledByClientResponse extends ErrorCodeHelper {
    private List<MissionCanceledByClientInfo> missionCanceledByClientInfos = new ArrayList<MissionCanceledByClientInfo>();

    public List<MissionCanceledByClientInfo> getMissionCanceledByClientInfos() {
        return missionCanceledByClientInfos;
    }

    public void setMissionCanceledByClientInfos(List<MissionCanceledByClientInfo> missionCanceledByClientInfos) {
        this.missionCanceledByClientInfos = missionCanceledByClientInfos;
    }
}
