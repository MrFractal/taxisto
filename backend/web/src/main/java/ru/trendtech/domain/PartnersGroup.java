package ru.trendtech.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by petr on 19.09.2014.
 */

@Entity
@Table(name = "partners_group")
public class PartnersGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "group_name", nullable = false)
    private String groupName;

    @Column(name = "section", nullable = false)
    private String section;

    @OneToMany
    @JoinColumn(name = "group_id", insertable = true, updatable = true)
    private List<ItemPartnersGroup> groupItems = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public List<ItemPartnersGroup> getGroupItems() {
        return groupItems;
    }

    public void setGroupItems(List<ItemPartnersGroup> groupItems) {
        this.groupItems = groupItems;
    }
}

