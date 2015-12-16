package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 08.06.2015.
 */
public class MissionCanceledByClientInfo {
    private String date;
    private String count;

    public MissionCanceledByClientInfo(String date, String count) {
        this.date = date;
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
