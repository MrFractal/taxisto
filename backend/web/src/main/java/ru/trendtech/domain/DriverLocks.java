package ru.trendtech.domain;

import javax.persistence.*;

/**
 * Created by petr on 07.08.14.
 */

@Entity
@Table(name = "driver_locks")
public class DriverLocks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "driver_id")
    private long driverId;

    @Column(name = "time_of_lock")
    private Long timeOfLock;

    @Column(name = "reason")
    private String reason;

    @Column(name = "time_of_unlock")
    private Long timeOfUnlock;

    @Column(name = "mission_id")
    private Long missionId;


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

    @Override
    public String toString() {
        return "";
    }



}
