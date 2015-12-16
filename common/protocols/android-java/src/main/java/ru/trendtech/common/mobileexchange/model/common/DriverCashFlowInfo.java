package ru.trendtech.common.mobileexchange.model.common;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 10.02.2015.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverCashFlowInfo {
    private Long id;
    private MissionInfoARM missionInfoARM;
    private DriverInfoARM driverInfoARM; // на случай, когда mission_id = null
    private long driverPeriodWorkId;
    private int operation;
    private long dateOperation;
    private int sum;

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public DriverInfoARM getDriverInfoARM() {
        return driverInfoARM;
    }

    public void setDriverInfoARM(DriverInfoARM driverInfoARM) {
        this.driverInfoARM = driverInfoARM;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MissionInfoARM getMissionInfoARM() {
        return missionInfoARM;
    }

    public void setMissionInfoARM(MissionInfoARM missionInfoARM) {
        this.missionInfoARM = missionInfoARM;
    }

    public long getDriverPeriodWorkId() {
        return driverPeriodWorkId;
    }

    public void setDriverPeriodWorkId(long driverPeriodWorkId) {
        this.driverPeriodWorkId = driverPeriodWorkId;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public long getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(long dateOperation) {
        this.dateOperation = dateOperation;
    }
}
