package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 06.02.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TipPercentInfo {
    private Long id;
    private int percent;
    private int sumByPercent;

    public int getSumByPercent() {
        return sumByPercent;
    }

    public void setSumByPercent(int sumByPercent) {
        this.sumByPercent = sumByPercent;
    }

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
