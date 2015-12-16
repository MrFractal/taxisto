package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 10.10.2014.
 */
public class RebornMissionRequest {
    private long missionId;
    private int sumIncrease;
    private String comment;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public int getSumIncrease() {
        return sumIncrease;
    }

    public void setSumIncrease(int sumIncrease) {
        this.sumIncrease = sumIncrease;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
