package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 20.07.2015.
 */
public class MissionCanceledInfo {
    private Long id;
    private String cancelBy;
    private Long missionId;
    private Long cancelById;
    private long timeOfCanceled;
    private String comment;
    private String stateBeforeCanceled;
    private ReasonInfo reasonInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCancelBy() {
        return cancelBy;
    }

    public void setCancelBy(String cancelBy) {
        this.cancelBy = cancelBy;
    }

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }

    public Long getCancelById() {
        return cancelById;
    }

    public void setCancelById(Long cancelById) {
        this.cancelById = cancelById;
    }

    public long getTimeOfCanceled() {
        return timeOfCanceled;
    }

    public void setTimeOfCanceled(long timeOfCanceled) {
        this.timeOfCanceled = timeOfCanceled;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStateBeforeCanceled() {
        return stateBeforeCanceled;
    }

    public void setStateBeforeCanceled(String stateBeforeCanceled) {
        this.stateBeforeCanceled = stateBeforeCanceled;
    }

    public ReasonInfo getReasonInfo() {
        return reasonInfo;
    }

    public void setReasonInfo(ReasonInfo reasonInfo) {
        this.reasonInfo = reasonInfo;
    }
}
