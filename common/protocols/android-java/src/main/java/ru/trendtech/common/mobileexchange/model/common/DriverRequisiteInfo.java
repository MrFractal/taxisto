package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 26.01.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverRequisiteInfo {
    private long id;
    private boolean staffer; // штатный/наемный
    private int salaryPerDay; // зарплата
    private int countMinutesOfRest; // кол-во минут отдыха
    private long driverId;
    private int startHours; // часы старта работы
    private int startMinutes; // минуты старта работы
    private int endHours; // часы окончания работы
    private int endMinutes; // минуты окончания работы
    private long updateTime;
    private boolean active;
    private long dismissalTime; // время приема/увольнения
    private int typeDismissal; // 1 - принят на работу, 0 - уволен
    private Integer typeSalary; // зп, %, и еще нечто


    public Integer getTypeSalary() {
        return typeSalary;
    }

    public void setTypeSalary(Integer typeSalary) {
        this.typeSalary = typeSalary;
    }

    public long getDismissalTime() {
        return dismissalTime;
    }

    public void setDismissalTime(long dismissalTime) {
        this.dismissalTime = dismissalTime;
    }

    public int getTypeDismissal() {
        return typeDismissal;
    }

    public void setTypeDismissal(int typeDismissal) {
        this.typeDismissal = typeDismissal;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isStaffer() {
        return staffer;
    }

    public void setStaffer(boolean staffer) {
        this.staffer = staffer;
    }

    public int getSalaryPerDay() {
        return salaryPerDay;
    }

    public void setSalaryPerDay(int salaryPerDay) {
        this.salaryPerDay = salaryPerDay;
    }

    public int getCountMinutesOfRest() {
        return countMinutesOfRest;
    }

    public void setCountMinutesOfRest(int countMinutesOfRest) {
        this.countMinutesOfRest = countMinutesOfRest;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public int getStartHours() {
        return startHours;
    }

    public void setStartHours(int startHours) {
        this.startHours = startHours;
    }

    public int getStartMinutes() {
        return startMinutes;
    }

    public void setStartMinutes(int startMinutes) {
        this.startMinutes = startMinutes;
    }

    public int getEndHours() {
        return endHours;
    }

    public void setEndHours(int endHours) {
        this.endHours = endHours;
    }

    public int getEndMinutes() {
        return endMinutes;
    }

    public void setEndMinutes(int endMinutes) {
        this.endMinutes = endMinutes;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
