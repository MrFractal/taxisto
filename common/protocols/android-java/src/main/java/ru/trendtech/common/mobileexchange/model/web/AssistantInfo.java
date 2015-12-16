package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 14.11.2014.
 */
public class AssistantInfo {
    private Long id;
    private String name;
    private int tabletCount;


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
