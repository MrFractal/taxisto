package ru.trendtech.common.mobileexchange.model.courier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 07.10.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemPropertyInfo {
    private long id;
    private String namePoperty;
    private boolean alcohol; // 1 - данный товар бухло

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNamePoperty() {
        return namePoperty;
    }

    public void setNamePoperty(String namePoperty) {
        this.namePoperty = namePoperty;
    }

    public boolean isAlcohol() {
        return alcohol;
    }

    public void setAlcohol(boolean alcohol) {
        this.alcohol = alcohol;
    }
}
