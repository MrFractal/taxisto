package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.courier.web.CommonRequest;

/**
 * Created by petr on 19.09.2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LateDriverBookedRequest extends CommonRequest {
   private int lateDriverBookedMin = 0;
   private long missionId;

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public int getLateDriverBookedMin() {
        return lateDriverBookedMin;
    }

    public void setLateDriverBookedMin(int lateDriverBookedMin) {
        this.lateDriverBookedMin = lateDriverBookedMin;
    }
}
