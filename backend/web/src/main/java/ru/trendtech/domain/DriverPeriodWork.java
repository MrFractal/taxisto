package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 30.01.2015.
 */
@Entity
@Table(name = "driver_period_work")
public class DriverPeriodWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start_work", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime startWork;

    @Column(name = "end_work", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime endWork;

    @Column(name = "active", nullable = false, columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean active;

    @Column(name = "time_sec_rest", nullable = false)
    private int timeSecRest;

    @Column(name = "time_sec_work", nullable = false)
    private int timeSecWork;

    @Column(name = "time_sec_pay_rest", nullable = false)
    private int timeSecPayRest;

    @OneToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @Column(name = "update_time")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime updateTime;

    @Column(name = "time_work_in_fact_start")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeWorkInFactOfStarting;

    @Column(name = "time_work_in_fact_end")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeWorkInFactOfEnding;

    @Column(name = "last_time_pay_rest")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lastTimePayRest;

    @OneToOne
    @JoinColumn(name = "requisite_id")
    private DriverRequisite driverRequisite;

    public DriverRequisite getDriverRequisite() {
        return driverRequisite;
    }

    public void setDriverRequisite(DriverRequisite driverRequisite) {
        this.driverRequisite = driverRequisite;
    }

    public DateTime getLastTimePayRest() {
        return lastTimePayRest;
    }

    public void setLastTimePayRest(DateTime lastTimePayRest) {
        this.lastTimePayRest = lastTimePayRest;
    }

    public DateTime getTimeWorkInFactOfStarting() {
        return timeWorkInFactOfStarting;
    }

    public void setTimeWorkInFactOfStarting(DateTime timeWorkInFactOfStarting) {
        this.timeWorkInFactOfStarting = timeWorkInFactOfStarting;
    }

    public DateTime getTimeWorkInFactOfEnding() {
        return timeWorkInFactOfEnding;
    }

    public void setTimeWorkInFactOfEnding(DateTime timeWorkInFactOfEnding) {
        this.timeWorkInFactOfEnding = timeWorkInFactOfEnding;
    }

    public DateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(DateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getStartWork() {
        return startWork;
    }

    public void setStartWork(DateTime startWork) {
        this.startWork = startWork;
    }

    public DateTime getEndWork() {
        return endWork;
    }

    public void setEndWork(DateTime endWork) {
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

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
