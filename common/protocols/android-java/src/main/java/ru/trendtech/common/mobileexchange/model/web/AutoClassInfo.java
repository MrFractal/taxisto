package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 16.12.2014.
 */
public class AutoClassInfo {
    private long mission_rate_id=1;
    private String auto_class;
    private int free_wait_minutes;
    private int intercity;
    private int km_included;
    private int per_hour_amount;
    private int per_minute_wait_amount;
    private int price;
    private int price_hour;
    private int price_km;

    public long getMission_rate_id() {
        return mission_rate_id;
    }

    public void setMission_rate_id(long mission_rate_id) {
        this.mission_rate_id = mission_rate_id;
    }

    public String getAuto_class() {
        return auto_class;
    }

    public void setAuto_class(String auto_class) {
        this.auto_class = auto_class;
    }

    public int getFree_wait_minutes() {
        return free_wait_minutes;
    }

    public void setFree_wait_minutes(int free_wait_minutes) {
        this.free_wait_minutes = free_wait_minutes;
    }

    public int getIntercity() {
        return intercity;
    }

    public void setIntercity(int intercity) {
        this.intercity = intercity;
    }

    public int getKm_included() {
        return km_included;
    }

    public void setKm_included(int km_included) {
        this.km_included = km_included;
    }

    public int getPer_hour_amount() {
        return per_hour_amount;
    }

    public void setPer_hour_amount(int per_hour_amount) {
        this.per_hour_amount = per_hour_amount;
    }

    public int getPer_minute_wait_amount() {
        return per_minute_wait_amount;
    }

    public void setPer_minute_wait_amount(int per_minute_wait_amount) {
        this.per_minute_wait_amount = per_minute_wait_amount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice_hour() {
        return price_hour;
    }

    public void setPrice_hour(int price_hour) {
        this.price_hour = price_hour;
    }

    public int getPrice_km() {
        return price_km;
    }

    public void setPrice_km(int price_km) {
        this.price_km = price_km;
    }
}
