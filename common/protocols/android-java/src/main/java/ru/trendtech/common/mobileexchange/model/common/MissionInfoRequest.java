package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by max on 13.02.14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MissionInfoRequest {
    private long missionId;

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }
}
