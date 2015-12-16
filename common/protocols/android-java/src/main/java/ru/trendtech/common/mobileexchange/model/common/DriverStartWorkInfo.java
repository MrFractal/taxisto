package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 30.01.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverStartWorkInfo {
    private long id;
    private long startWork;
    private long endWork;
    private boolean active;
    private int timeSecRest;
    private int timeSecWork;
    private int timeSecPayRest;
    private long driverId;
    private long updateTime;
    private long timeWorkInFactOfStarting;
    private long timeWorkInFactOfEnding;
    private boolean isFuture; // true - будущая, false - текущая
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isFuture() {
        return isFuture;
    }

    public void setFuture(boolean isFuture) {
        this.isFuture = isFuture;
    }

    public long getTimeWorkInFactOfStarting() {
        return timeWorkInFactOfStarting;
    }

    public void setTimeWorkInFactOfStarting(long timeWorkInFactOfStarting) {
        this.timeWorkInFactOfStarting = timeWorkInFactOfStarting;
    }

    public long getTimeWorkInFactOfEnding() {
        return timeWorkInFactOfEnding;
    }

    public void setTimeWorkInFactOfEnding(long timeWorkInFactOfEnding) {
        this.timeWorkInFactOfEnding = timeWorkInFactOfEnding;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStartWork() {
        return startWork;
    }

    public void setStartWork(long startWork) {
        this.startWork = startWork;
    }

    public long getEndWork() {
        return endWork;
    }

    public void setEndWork(long endWork) {
        this.endWork = endWork;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getTimeSecRest() {
        return timeSecRest;
    }

    public void setTimeSecRest(int timeSecRest) {
        this.timeSecRest = timeSecRest;
    }

    public int getTimeSecWork() {
        return timeSecWork;
    }

    public void setTimeSecWork(int timeSecWork) {
        this.timeSecWork = timeSecWork;
    }

    public int getTimeSecPayRest() {
        return timeSecPayRest;
    }

    public void setTimeSecPayRest(int timeSecPayRest) {
        this.timeSecPayRest = timeSecPayRest;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
