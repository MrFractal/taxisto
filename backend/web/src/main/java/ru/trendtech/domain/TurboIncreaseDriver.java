package ru.trendtech.domain;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

import javax.persistence.*;

/**
 * Created by petr on 18.12.2014.
 */

@Entity
@Table(name = "turbo_increase_driver")
public class TurboIncreaseDriver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @OneToOne
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Columns(columns = {@Column(name = "sum_increase_currency"), @Column(name = "sum_increase")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmountAndCurrency")
    private Money sumIncreaseDriver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public Money getSumIncreaseDriver() {
        return sumIncreaseDriver;
    }

    public void setSumIncreaseDriver(Money sumIncreaseDriver) {
        this.sumIncreaseDriver = sumIncreaseDriver;
    }
}
