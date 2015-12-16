package ru.trendtech.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by petr on 29.03.2015.
 */
@Entity
@Table(name = "region")
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name_region")
    private String nameRegion;

    @Column(name = "coast")
    private String coast;

    @Column(name = "markup", nullable = false, columnDefinition = "int default 0")
    private int markup;

    @Column(name = "is_active", nullable = false, columnDefinition = "BIT DEFAULT 1", length = 1)
    private boolean isActive;

    @Column(name = "type_region", nullable = false, columnDefinition = "int default 0")
    private int typeRegion; // 0 - местный, 1 - аэро, 2 - межгород

    @JsonIgnore
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "region_coordinates", joinColumns = {@JoinColumn(name = "region_id")})
    private List<String> regionCoordinates = new ArrayList<>();

    @Column(name = "to_markup", nullable = false, columnDefinition = "int default 0")
    private int toMarkup;

    @Column(name = "from_markup", nullable = false, columnDefinition = "int default 0")
    private int fromMarkup;

    @Column(name = "radius", nullable = false, columnDefinition = "int default 0")
    private int radius;

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getToMarkup() {
        return toMarkup;
    }

    public void setToMarkup(int toMarkup) {
        this.toMarkup = toMarkup;
    }

    public int getFromMarkup() {
        return fromMarkup;
    }

    public void setFromMarkup(int fromMarkup) {
        this.fromMarkup = fromMarkup;
    }

    public int getMarkup() {
        return markup;
    }

    public void setMarkup(int markup) {
        this.markup = markup;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getTypeRegion() {
        return typeRegion;
    }

    public void setTypeRegion(int typeRegion) {
        this.typeRegion = typeRegion;
    }

    public String getCoast() {
        return coast;
    }

    public void setCoast(String coast) {
        this.coast = coast;
    }

    public List<String> getRegionCoordinates() {
        return regionCoordinates;
    }

    public void setRegionCoordinates(List<String> regionCoordinates) {
        this.regionCoordinates = regionCoordinates;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameRegion() {
        return nameRegion;
    }

    public void setNameRegion(String nameRegion) {
        this.nameRegion = nameRegion;
    }
}
