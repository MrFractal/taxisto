package ru.trendtech.common.mobileexchange.model.common.states;

import ru.trendtech.common.mobileexchange.model.common.MissionInfo;
import ru.trendtech.common.mobileexchange.model.common.rates.PaymentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 15.05.2015.
 */
public class ServerStateInfoV2 {
    private final long state;
    private MissionInfo missionInfo;
    private PaymentInfo paymentInfo;
    private long lastPauseStartTime;
    private List<MissionInfo> activeMissionInfos = new ArrayList<MissionInfo>();

    public List<MissionInfo> getActiveMissionInfos() {
        return activeMissionInfos;
    }


    public ServerStateInfoV2() {
        this(0);
    }

    public ServerStateInfoV2(long state) {
        this(state, null);
    }

    public ServerStateInfoV2(long state, MissionInfo missionInfo) {
        this(state, missionInfo, null, 0, null);
    }

    public ServerStateInfoV2(long state, MissionInfo missionInfo, PaymentInfo paymentInfo, long lastPauseStartTime, List<MissionInfo> activeMissionInfos) {
        this.lastPauseStartTime = lastPauseStartTime;
        this.state = state;
        this.missionInfo = missionInfo;
        this.paymentInfo = paymentInfo;
        this.activeMissionInfos = activeMissionInfos;
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
