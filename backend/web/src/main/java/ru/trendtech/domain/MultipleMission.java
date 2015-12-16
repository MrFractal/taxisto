package ru.trendtech.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by petr on 29.04.2015.
 */
@Entity
@Table(name = "multiple_mission")
public class MultipleMission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany
    @JoinColumn(name = "multiple_id") // , insertable = true, updatable = true
    private Set<Mission> multipleMissions = new HashSet<>();

    @Column(name = "description")
    private String description;


    public Set<Mission> getMultipleMissions() {
        return multipleMissions;
    }

    public void setMultipleMissions(Set<Mission> multipleMissions) {
        this.multipleMissions = multipleMissions;
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


}
