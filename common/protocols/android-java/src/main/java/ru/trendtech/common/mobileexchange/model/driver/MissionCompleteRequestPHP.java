package ru.trendtech.common.mobileexchange.model.driver;

import ru.trendtech.common.mobileexchange.model.common.DriverRequest;
import ru.trendtech.common.mobileexchange.model.common.rates.PaymentInfo;

public class MissionCompleteRequestPHP extends DriverRequest {
    private long missionId;
    private PaymentInfo paymentInfo;

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public long getMissionId() {
        return missionId;
    }


    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }
}
