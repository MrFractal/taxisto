package ru.trendtech.common.mobileexchange.model.driver;

/**
 * Created by petr on 12.05.2015.
 */
public class TimersDataInfo {
    public long driverId;
    public int secondsStateOnline;
    public int secondsStateBusy;
    public long timeWork;
    public long timeFreeRest;
    public long timePayRest;
    public String date; // 2015-05-09
    public long updateTime;
    public long periodWorkId;

    public long getPeriodWorkId() {
        return periodWorkId;
    }

    public void setPeriodWorkId(long periodWorkId) {
        this.periodWorkId = periodWorkId;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public int getSecondsStateOnline() {
        return secondsStateOnline;
    }

    public void setSecondsStateOnline(int secondsStateOnline) {
        this.secondsStateOnline = secondsStateOnline;
    }

    public int getSecondsStateBusy() {
        return secondsStateBusy;
    }

    public void setSecondsStateBusy(int secondsStateBusy) {
        this.secondsStateBusy = secondsStateBusy;
    }

    public long getTimeWork() {
        return timeWork;
    }

    public void setTimeWork(long timeWork) {
        this.timeWork = timeWork;
    }

    public long getTimeFreeRest() {
        return timeFreeRest;
    }

    public void setTimeFreeRest(long timeFreeRest) {
        this.timeFreeRest = timeFreeRest;
    }

    public long getTimePayRest() {
        return timePayRest;
    }

    public void setTimePayRest(long timePayRest) {
        this.timePayRest = timePayRest;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
