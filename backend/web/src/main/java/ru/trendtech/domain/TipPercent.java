package ru.trendtech.domain;

import javax.persistence.*;

/**
 * Created by petr on 06.02.2015.
 */
@Entity
@Table(name = "tip_percent")
public class TipPercent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "percent")
    private int percent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
