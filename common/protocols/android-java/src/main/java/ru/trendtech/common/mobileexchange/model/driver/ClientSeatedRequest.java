package ru.trendtech.common.mobileexchange.model.driver;

import ru.trendtech.common.mobileexchange.model.common.DriverRequest;

/**
 * Created by max on 13.02.14.
 */
public class ClientSeatedRequest extends DriverRequest {
    private long missionId;

    public ClientSeatedRequest() {
        super();
    }

    public ClientSeatedRequest(long driverId, long missionId) {
        super(driverId);
        this.missionId = missionId;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }
}
