package ru.trendtech.domain.billing;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.Mission;

import javax.persistence.*;

/**
 * Created by petr on 29.08.14.
 */

@Entity
@Table(name = "mission_cost_increase")
public class MissionCostIncrease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "mission_id", nullable = false, unique = true)
    private Mission mission;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Column(name = "sum_expected")
    private Double sumExpected;

    @Column(name = "sum_increase", nullable = false)
    private Double sumIncrease;

    @Column(name = "time_created", nullable = false)
    private Long timeCreated;

    @Column(name = "comment")
    private String comment;


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Double getSumExpected() {
        return sumExpected;
    }

    public void setSumExpected(Double sumExpected) {
        this.sumExpected = sumExpected;
    }

    public Double getSumIncrease() {
        return sumIncrease;
    }

    public void setSumIncrease(Double sumIncrease) {
        this.sumIncrease = sumIncrease;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }
}
