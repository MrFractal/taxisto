package ru.trendtech.domain;

import javax.persistence.*;

/**
 * Created by petr on 19.12.2014.
 */
@Entity
@Table(name = "alternative_statistics")
public class AlternativeStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type_stat", nullable = false)
    private String typeStat;

    @Column(name = "type_stat_value", nullable = false)
    private String typeStatValue;

    @Column(name = "time_of_stat")
    private Long timeOfStat;

    public AlternativeStatistics(){
    }

    public AlternativeStatistics(String typeStat, String typeStatValue, long timeOfStat){
        this.typeStat = typeStat;
        this.typeStatValue = typeStatValue;
        this.timeOfStat = timeOfStat;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeStat() {
        return typeStat;
    }

    public void setTypeStat(String typeStat) {
        this.typeStat = typeStat;
    }

    public String getTypeStatValue() {
        return typeStatValue;
    }

    public void setTypeStatValue(String typeStatValue) {
        this.typeStatValue = typeStatValue;
    }

    public Long getTimeOfStat() {
        return timeOfStat;
    }

    public void setTimeOfStat(Long timeOfStat) {
        this.timeOfStat = timeOfStat;
    }
}
