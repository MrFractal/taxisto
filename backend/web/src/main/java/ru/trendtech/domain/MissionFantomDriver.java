package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
/**
 * Created by petr on 08.06.2015.
 */
@Entity
@Table(name ="mission_fantom_driver")
public class MissionFantomDriver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "time_assigning")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfAssigning;

    @OneToOne
    @JoinColumn(name = "mission_id", unique = true)
    private Mission mission;

    @OneToOne
    @JoinColumn(name = "f_driver_id")
    private Driver fantomDiver;

    @OneToOne
    @JoinColumn(name = "driver_id")
    private Driver diver;

    @Column(name = "general_sec_search", columnDefinition = "int default 0")
    private int generalTimeSecSearch;

    @Column(name = "sum_increase", columnDefinition = "int default 0")
    private int sumIncrease;

    @Column(name = "calc_time_arrival", columnDefinition = "int default 0")
    private int calculateTimeArrival;

    public int getCalculateTimeArrival() {
        return calculateTimeArrival;
    }

    public void setCalculateTimeArrival(int calculateTimeArrival) {
        this.calculateTimeArrival = calculateTimeArrival;
    }

    public int getSumIncrease() {
        return sumIncrease;
    }

    public void setSumIncrease(int sumIncrease) {
        this.sumIncrease = sumIncrease;
    }

    public int getGeneralTimeSecSearch() {
        return generalTimeSecSearch;
    }

    public void setGeneralTimeSecSearch(int generalTimeSecSearch) {
        this.generalTimeSecSearch = generalTimeSecSearch;
    }

    public Driver getDiver() {
        return diver;
    }

    public void setDiver(Driver diver) {
        this.diver = diver;
    }

    public MissionFantomDriver(DateTime timeOfAssigning, Mission mission, Driver fantomDiver) {
        this.timeOfAssigning = timeOfAssigning;
        this.mission = mission;
        this.fantomDiver = fantomDiver;
    }

    public MissionFantomDriver() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getTimeOfAssigning() {
        return timeOfAssigning;
    }

    public void setTimeOfAssigning(DateTime timeOfAssigning) {
        this.timeOfAssigning = timeOfAssigning;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public Driver getFantomDiver() {
        return fantomDiver;
    }

    public void setFantomDiver(Driver fantomDiver) {
        this.fantomDiver = fantomDiver;
    }
}
