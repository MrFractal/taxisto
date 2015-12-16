package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 08.07.2015.
 */
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    private Mission mission;

    @Column(name = "push_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PushType pushType;

    @Column(name = "description")
    private String description;

    @Column(name = "time_notification")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfNotification;

    public DateTime getTimeOfNotification() {
        return timeOfNotification;
    }

    public void setTimeOfNotification(DateTime timeOfNotification) {
        this.timeOfNotification = timeOfNotification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public PushType getPushType() {
        return pushType;
    }

    public void setPushType(PushType pushType) {
        this.pushType = pushType;
    }
}
