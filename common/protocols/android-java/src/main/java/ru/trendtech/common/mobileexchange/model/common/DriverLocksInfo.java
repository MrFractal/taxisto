package ru.trendtech.common.mobileexchange.model.common;

import ru.trendtech.common.mobileexchange.model.client.CardInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 07.08.14.
 */
public class DriverLocksInfo {
    private long id;
    private long driverId;
    private Long timeOfLock;
    private String reason;
    private Long timeOfUnlock;
    private Long missionId;
    private String administrativeStatus;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public Long getTimeOfLock() {
        return timeOfLock;
    }

    public void setTimeOfLock(Long timeOfLock) {
        this.timeOfLock = timeOfLock;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getTimeOfUnlock() {
        return timeOfUnlock;
    }

    public void setTimeOfUnlock(Long timeOfUnlock) {
        this.timeOfUnlock = timeOfUnlock;
    }

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }

    public String getAdministrativeStatus() {
        return administrativeStatus;
    }

    public void setAdministrativeStatus(String administrativeStatus) {
        this.administrativeStatus = administrativeStatus;
    }
}
