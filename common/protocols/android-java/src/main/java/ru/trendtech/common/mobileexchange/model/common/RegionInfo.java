package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 15.04.2015.
 */
public class RegionInfo {
    private long id;
    private String nameRegion;
    private String base64Coord;
    private String coast; // left, right
    private Integer markup;
    private Boolean active;
    private Integer typeRegion; // 0 - местный, 1 - аэропорт, 2 - удаленный
    private Integer toMarkup;
    private Integer fromMarkup;
    private Integer radius;

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public Integer getToMarkup() {
        return toMarkup;
    }

    public void setToMarkup(Integer toMarkup) {
        this.toMarkup = toMarkup;
    }

    public Integer getFromMarkup() {
        return fromMarkup;
    }

    public void setFromMarkup(Integer fromMarkup) {
        this.fromMarkup = fromMarkup;
    }

    public Integer getMarkup() {
        return markup;
    }

    public void setMarkup(Integer markup) {
        this.markup = markup;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getTypeRegion() {
        return typeRegion;
    }

    public void setTypeRegion(Integer typeRegion) {
        this.typeRegion = typeRegion;
    }

    public String getCoast() {
        return coast;
    }

    public void setCoast(String coast) {
        this.coast = coast;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBase64Coord() {
        return base64Coord;
    }

    public void setBase64Coord(String base64Coord) {
        this.base64Coord = base64Coord;
    }

    public String getNameRegion() {
        return nameRegion;
    }

    public void setNameRegion(String nameRegion) {
        this.nameRegion = nameRegion;
    }
}
