package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 18.12.2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TurboIncreaseDriverInfo {
    private Long id;
    private long driverId;
    private long missionId;
    private int sumIncreaseDriver;
    private int timeBeforeArrival;

    public int getTimeBeforeArrival() {
        return timeBeforeArrival;
    }

    public void setTimeBeforeArrival(int timeBeforeArrival) {
        this.timeBeforeArrival = timeBeforeArrival;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public int getSumIncreaseDriver() {
        return sumIncreaseDriver;
    }

    public void setSumIncreaseDriver(int sumIncreaseDriver) {
        this.sumIncreaseDriver = sumIncreaseDriver;
    }
}
