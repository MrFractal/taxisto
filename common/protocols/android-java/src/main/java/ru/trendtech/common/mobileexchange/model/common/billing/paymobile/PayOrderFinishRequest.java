package ru.trendtech.common.mobileexchange.model.common.billing.paymobile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.DriverRequest;

/**
 * Created by petr on 06.02.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayOrderFinishRequest extends DriverRequest {
    private long missionId;

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }
}
