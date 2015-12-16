package ru.trendtech.common.mobileexchange.model.common.states;

import ru.trendtech.common.mobileexchange.model.common.MissionInfo;
import ru.trendtech.common.mobileexchange.model.common.rates.PaymentInfo;

/**
 * File created by max on 21/05/2014 14:22.
 */


public class ServerStateInfo {
    private final long state;
    private MissionInfo missionInfo;
    private PaymentInfo paymentInfo;
    private long lastPauseStartTime;

    public ServerStateInfo() {
        this(0);
    }

    public ServerStateInfo(long state) {
        this(state, null);
    }

    public ServerStateInfo(long state, MissionInfo missionInfo) {
        this(state, missionInfo, null, 0);
    }

    public ServerStateInfo(long state, MissionInfo missionInfo, PaymentInfo paymentInfo, long lastPauseStartTime) {
        this.lastPauseStartTime = lastPauseStartTime;
        this.state = state;
        this.missionInfo = missionInfo;
        this.paymentInfo = paymentInfo;
    }


    public MissionInfo getMissionInfo() {
        return missionInfo;
    }

    public long getState() {
        return state;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public long getLastPauseStartTime() {
        return lastPauseStartTime;
    }

    public void setLastPauseStartTime(long lastPauseStartTime) {
        this.lastPauseStartTime = lastPauseStartTime;
    }
}
