package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 12.05.2015.
 */
@Entity
@Table(name = "declined_mission")
public class DeclinedMission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @OneToOne
    @JoinColumn(name = "take_driver_id")
    private Driver takeDriver;

    @OneToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;

//    @OneToOne
//    @JoinColumn(name = "reason_id")
//    private Reason reason;

    @Column(name = "time_declined")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfDeclined;
}
