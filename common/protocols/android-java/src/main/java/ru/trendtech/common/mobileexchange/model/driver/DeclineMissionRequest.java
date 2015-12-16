package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.DriverRequest;

/**
 * Created by max on 09.02.14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeclineMissionRequest extends DriverRequest {
    private long missionId;

    public DeclineMissionRequest(long driverId, long missionId) {
        super(driverId);
        this.missionId = missionId;
    }

    public DeclineMissionRequest() {
        super();
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }
}
