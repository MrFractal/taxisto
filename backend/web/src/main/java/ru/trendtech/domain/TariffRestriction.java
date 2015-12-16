package ru.trendtech.domain;

import javax.persistence.*;

/**
 * Created by petr on 11.03.2015.
 */
@Entity
@Table(name = "tariff_restriction")
public class TariffRestriction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tariff_name", nullable = false)
    private String tariffName;

    @Column(name = "start_hours", nullable = false)
    private int startHours;

    @Column(name = "start_minutes", nullable = false)
    private int startMinutes;

    @Column(name = "end_hours", nullable = false)
    private int endHours;

    @Column(name = "end_minutes", nullable = false)
    private int endMinutes;

    @Column(name = "holiday", nullable = false, columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean holiday;

    @Column(name = "active", nullable = false, columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean active;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTariffName() {
        return tariffName;
    }

    public void setTariffName(String tariffName) {
        this.tariffName = tariffName;
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

    public boolean isHoliday() {
        return holiday;
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
