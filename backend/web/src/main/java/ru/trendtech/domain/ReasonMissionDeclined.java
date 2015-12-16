package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 30.03.2015.
 */
@Entity
@Table(name = "reason_mission_declined")
public class ReasonMissionDeclined {
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

    @Column(name = "type_reason", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TypeReason typeReason;

    @Column(name = "time_declined")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfDeclined;


    public Driver getTakeDriver() {
        return takeDriver;
    }

    public void setTakeDriver(Driver takeDriver) {
        this.takeDriver = takeDriver;
    }

    public TypeReason getTypeReason() {
        return typeReason;
    }

    public void setTypeReason(TypeReason typeReason) {
        this.typeReason = typeReason;
    }

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

    public DateTime getTimeOfDeclined() {
        return timeOfDeclined;
    }

    public void setTimeOfDeclined(DateTime timeOfDeclined) {
        this.timeOfDeclined = timeOfDeclined;
    }



    public enum TypeReason {
        UNKNOWN(0),
        CRASH(1),
        CLIENT_NOT_WANT_WAIT(2),
        WANT_SMOKE(3),
        CLIENT_NOT_RESPONDE(4),
        CLIENT_NOT_GO_OUT(5),
        ;

        private final int value;

        private TypeReason(int value) {
            this.value = value;
        }

        public static TypeReason getByValue(int value) {
            TypeReason result = UNKNOWN;
            for (TypeReason item : values()) {
                if (item.getValue() == value) {
                    result = item;
                    break;
                }
            }
            return result;
        }

        public int getValue() {
            return value;
        }
    }
}
