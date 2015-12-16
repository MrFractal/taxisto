package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by max on 13.02.14.
 */
public class CancelMissionResponse extends DumbResponse{
    private long missionId;
    private boolean completed;
    //f:add
    private String comment;
    private int reason;
    private long initiatorId;
    //private ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();
    //f:add

//
//    public ErrorCodeHelper getErrorCodeHelper() {
//        return errorCodeHelper;
//    }
//
//    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
//        this.errorCodeHelper = errorCodeHelper;
//    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public long getInitiatorId() {
        return initiatorId;
    }

    public void setInitiatorId(long initiatorId) {
        this.initiatorId = initiatorId;
    }
}
