package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 27.04.2015.
 */
public class PeriodWorkInfo {
    private long id;
    private long startPeriod;
    private long endPeriod;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(long startPeriod) {
        this.startPeriod = startPeriod;
    }

    public long getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(long endPeriod) {
        this.endPeriod = endPeriod;
    }
}
