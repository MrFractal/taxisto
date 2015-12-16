package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import javax.persistence.*;
/**
 * Created by petr on 01.04.2015.
 */
@Entity
@Table(name = "queue_mission")
public class QueueMission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @OneToOne
    @JoinColumn(name = "mission_id", nullable = false, unique = true)
    private Mission mission;

    @Column(name = "time_assigning")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfAssigning;

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

    public DateTime getTimeOfAssigning() {
        return timeOfAssigning;
    }

    public void setTimeOfAssigning(DateTime timeOfAssigning) {
        this.timeOfAssigning = timeOfAssigning;
    }
}
