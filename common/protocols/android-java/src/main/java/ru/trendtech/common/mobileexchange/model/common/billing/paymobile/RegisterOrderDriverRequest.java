package ru.trendtech.common.mobileexchange.model.common.billing.paymobile;

/**
 * Created by petr on 15.09.2014.
 */

public class RegisterOrderDriverRequest {
    private long driverId;
    private long missionId;
    private int sum;

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

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

}
