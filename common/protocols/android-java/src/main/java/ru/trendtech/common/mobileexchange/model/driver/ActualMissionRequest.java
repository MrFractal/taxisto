package ru.trendtech.common.mobileexchange.model.driver;

import ru.trendtech.common.mobileexchange.model.common.DriverRequest;

/**
 * Created by petr on 24.09.2014.
 */

public class ActualMissionRequest extends DriverRequest {
    private long missionId;

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }
}
