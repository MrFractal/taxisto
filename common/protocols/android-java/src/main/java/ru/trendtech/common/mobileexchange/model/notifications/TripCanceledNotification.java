package ru.trendtech.common.mobileexchange.model.notifications;

/**
 * Created by ivanenok on 4/14/2014.
 */
public class TripCanceledNotification {
    private long missionId;
    private int reason;
    private String reasonMsg;

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public String getReasonMsg() {
        return reasonMsg;
    }

    public void setReasonMsg(String reasonMsg) {
        this.reasonMsg = reasonMsg;
    }
}
