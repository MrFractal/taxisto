package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 28.01.2015.
 */

@Entity
@Table(name = "ban_period_rest_driver")
public class BanPeriodRestDriver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "time_start")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfStarting;

    @Column(name = "time_end")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfEnding;

    @Column(name = "active", columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getTimeOfStarting() {
        return timeOfStarting;
    }

    public void setTimeOfStarting(DateTime timeOfStarting) {
        this.timeOfStarting = timeOfStarting;
    }

    public DateTime getTimeOfEnding() {
        return timeOfEnding;
    }

    public void setTimeOfEnding(DateTime timeOfEnding) {
        this.timeOfEnding = timeOfEnding;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

/*
Необходим механизм "запретных периодов для отдыха" водителей.
Сделать таблицу с указанием "запретных периодов" для отдыха водителям:
Содержание таблицы:
1. Дата и время начала
2. Дата и время окончания
3. Признак активности (булево)
Этот механизм необходим для зарплатных водителей.
Также нужны методы бэкенда (создание, изменение, чтение).
 */