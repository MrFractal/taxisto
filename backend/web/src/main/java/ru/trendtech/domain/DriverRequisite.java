package ru.trendtech.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 26.01.2015.
 */
@Entity
@Table(name = "driver_requisite")
public class DriverRequisite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "staffer", nullable = false, columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean staffer; // штатный/наемный

    @Column(name = "salary_per_day", nullable = false)
    private int salaryPerDay; // зарплата

    @Column(name = "count_minutes_rest", nullable = false)
    private int countMinutesOfRest; // кол-во минут отдыха

    @OneToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @Column(name = "start_hours", nullable = false)
    private int startHours; // часы старта работы

    @Column(name = "start_minutes", nullable = false)
    private int startMinutes; // минуты старта работы

    @Column(name = "end_hours", nullable = false)
    private int endHours; // часы окончания работы

    @Column(name = "end_minutes", nullable = false)
    private int endMinutes; // минуты окончания работы

    @Column(name = "update_time")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime updateTime;

    @Column(name = "dismissal_time", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime dismissalTime; // время приема

    @Column(name = "type_dismissal", nullable = false)
    private int typeDismissal; // 1 - принят на работу, 0 - уволен

    @Column(name = "active", nullable = false, columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean active;

    /* зарплатники, процентники и нечто 3-ее*/
    @Column(name = "type_salary", nullable = false, columnDefinition = "int default 0")
    private int typeSalary;

    @Column(name = "salary_priority", nullable = false, columnDefinition = "int default 0")
    private int salaryPriority;

    public int getSalaryPriority() {
        return salaryPriority;
    }

    public void setSalaryPriority(int salaryPriority) {
        this.salaryPriority = salaryPriority;
    }

    public int getTypeSalary() {
        return typeSalary;
    }

    public void setTypeSalary(int typeSalary) {
        this.typeSalary = typeSalary;
    }

    public int getTypeDismissal() {
        return typeDismissal;
    }

    public void setTypeDismissal(int typeDismissal) {
        this.typeDismissal = typeDismissal;
    }

    public DateTime getDismissalTime() {
        return dismissalTime;
    }

    public void setDismissalTime(DateTime dismissalTime) {
        this.dismissalTime = dismissalTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
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

    public DateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(DateTime updateTime) {
        this.updateTime = updateTime;
    }
}
/*
1. Водитель штатный/наемный
2. Размер суточной ЗП
3. Количество минут отдыха в сутки
4. Начало и конец суток для водителя
5. Водитель
 */