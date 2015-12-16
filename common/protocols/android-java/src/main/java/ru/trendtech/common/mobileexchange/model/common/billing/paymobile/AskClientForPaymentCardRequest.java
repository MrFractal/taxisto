package ru.trendtech.common.mobileexchange.model.common.billing.paymobile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.DriverRequest;

/**
 * Created by petr on 06.02.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AskClientForPaymentCardRequest extends DriverRequest {
    private long driverId;
    private long missionId;
    private int sum;

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
