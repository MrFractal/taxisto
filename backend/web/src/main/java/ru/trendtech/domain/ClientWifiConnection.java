package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 06.07.2015.
 */
@Entity
@Table(name = "client_wifi_connection")
public class ClientWifiConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "time_of_opening", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfOpening;

    @Column(name = "time_of_closing", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfClosing;

    @OneToOne
    @JoinColumn(name = "mission_id", unique = true)
    private Mission mission;

    @Column(name = "description")
    private String description;

    @Column(name = "state")
    @Enumerated(value = EnumType.STRING)
    private StateConnection state = StateConnection.WAITING;


    public ClientWifiConnection(){

    }

    public ClientWifiConnection(DateTime timeOfOpening, Mission mission) {
        this.timeOfOpening = timeOfOpening;
        this.mission = mission;
    }

    public StateConnection getState() {
        return state;
    }

    public void setState(StateConnection state) {
        this.state = state;
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

    public DateTime getTimeOfOpening() {
        return timeOfOpening;
    }

    public void setTimeOfOpening(DateTime timeOfOpening) {
        this.timeOfOpening = timeOfOpening;
    }

    public DateTime getTimeOfClosing() {
        return timeOfClosing;
    }

    public void setTimeOfClosing(DateTime timeOfClosing) {
        this.timeOfClosing = timeOfClosing;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }


    public static enum StateConnection {
        WAITING,
        ONLINE,
        OFFLINE,
        ERROR,
        ;
    }
}
