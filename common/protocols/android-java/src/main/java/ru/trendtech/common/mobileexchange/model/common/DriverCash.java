package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverCash {
    private long id;
    private long driver_id;
    private Long mission_id;
    private int operation;
    private long dateOperation;
    private double sum;

    public DriverCash(long driver_id, Long mission_id, int operation, long dateOperation, double sum) {
        this.driver_id = driver_id;
        this.mission_id = mission_id;
        this.operation = operation;
        this.dateOperation = dateOperation;
        this.sum = sum;
    }

    public DriverCash(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(long driver_id) {
        this.driver_id = driver_id;
    }

    public Long getMission_id() {
        return mission_id;
    }

    public void setMission_id(Long mission_id) {
        this.mission_id = mission_id;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public long getDate() {
        return dateOperation;
    }

    public void setDate(long date) {
        this.dateOperation = date;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
