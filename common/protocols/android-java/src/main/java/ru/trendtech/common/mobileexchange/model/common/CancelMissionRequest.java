package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by max on 13.02.14.
 */
public class CancelMissionRequest extends DumbResponse{
    private long initiatorId;
    private long missionId;
    private String comment;
    private int reason; // for old version app
    private long reasonId = 0; // new version
    private boolean force;
    private String security_token;
    private String reasonStr;

    public long getReasonId() {
        return reasonId;
    }

    public void setReasonId(long reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonStr() {
        return reasonStr;
    }

    public void setReasonStr(String reasonStr) {
        this.reasonStr = reasonStr;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getInitiatorId() {
        return initiatorId;
    }

    public void setInitiatorId(long initiatorId) {
        this.initiatorId = initiatorId;
    }

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

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }
}
