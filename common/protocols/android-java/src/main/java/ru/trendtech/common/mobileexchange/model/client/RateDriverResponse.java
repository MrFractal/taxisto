package ru.trendtech.common.mobileexchange.model.client;

import ru.trendtech.common.mobileexchange.model.common.DumbResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 05.08.2014
 */
public class RateDriverResponse extends DumbResponse {

    private long missionId;
    private long clientId;
    private int wifi_quality;
    private int application_convenience;
    private int driver_courtesy;
    private int waiting_time;
    private int cleanliness;
    private int general;
    private String estimate_comment;

    public String getEstimate_comment() {
        return estimate_comment;
    }

    public void setEstimate_comment(String estimate_comment) {
        this.estimate_comment = estimate_comment;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public int getWifi_quality() {
        return wifi_quality;
    }

    public void setWifi_quality(int wifi_quality) {
        this.wifi_quality = wifi_quality;
    }

    public int getApplication_convenience() {
        return application_convenience;
    }

    public void setApplication_convenience(int application_convenience) {
        this.application_convenience = application_convenience;
    }

    public int getDriver_courtesy() {
        return driver_courtesy;
    }

    public void setDriver_courtesy(int driver_courtesy) {
        this.driver_courtesy = driver_courtesy;
    }

    public int getWaiting_time() {
        return waiting_time;
    }

    public void setWaiting_time(int waiting_time) {
        this.waiting_time = waiting_time;
    }

    public int getCleanliness() {
        return cleanliness;
    }

    public void setCleanliness(int cleanliness) {
        this.cleanliness = cleanliness;
    }

    public int getGeneral() {
        return general;
    }

    public void setGeneral(int general) {
        this.general = general;
    }
}
