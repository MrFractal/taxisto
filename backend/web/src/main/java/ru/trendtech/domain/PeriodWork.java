package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 27.04.2015.
 */
@Entity
@Table(name="period_work")
public class PeriodWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start_period")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime startPeriod;

    @Column(name = "end_period")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime endPeriod;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(DateTime startPeriod) {
        this.startPeriod = startPeriod;
    }

    public DateTime getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(DateTime endPeriod) {
        this.endPeriod = endPeriod;
    }
}
