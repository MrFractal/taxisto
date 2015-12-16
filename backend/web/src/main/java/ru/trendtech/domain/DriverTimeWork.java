package ru.trendtech.domain;

import org.joda.time.LocalDate;

import javax.persistence.*;

/**
 * Created by petr on 12.05.2015.
 */
@Entity
@Table(name = "driver_time_work")
public class DriverTimeWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    @Column(name = "time_sec_rest", nullable = false)
    private int timeSecRest;

    @Column(name = "time_sec_work", nullable = false)
    private int timeSecWork;

    @Column(name = "time_sec_pay_rest", nullable = false)
    private int timeSecPayRest;

    @Column(name = "sec_state_online", nullable = false)
    private int secondsStateOnline;

    @Column(name = "sec_stay_busy", nullable = false)
    private int secondsStateBusy;

    @OneToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
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
