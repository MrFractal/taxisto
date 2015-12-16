package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 31.03.2015.
 */
public class ActivityDriverRequest {
    private String security_token;
    private long driverId;
    private long startTime;
    private long endTime;
//    private int numberPage;
//    private int sizePage;


    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
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
}
