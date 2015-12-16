package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 17.12.2014.
 */
public class ComissionInfo {
    private long id;
    private long startTime;
    private long endTime;
    private int comissionAmount;
    private int comissionType;
    private long objectId; // id taxopark or id driver
    private long updateTime;

    public int getComissionType() {
        return comissionType;
    }

    public void setComissionType(int comissionType) {
        this.comissionType = comissionType;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getComissionAmount() {
        return comissionAmount;
    }

    public void setComissionAmount(int comissionAmount) {
        this.comissionAmount = comissionAmount;
    }

}

