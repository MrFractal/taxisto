package ru.trendtech.domain;

import javax.persistence.*;

/**
 * Created by petr on 14.11.2014.
 */


@Entity
@Table(name = "assistant")
public class Assistant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "tablet_count")
    private int tabletCount=0;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTabletCount() {
        return tabletCount;
    }

    public void setTabletCount(int tabletCount) {
        this.tabletCount = tabletCount;
    }
}
