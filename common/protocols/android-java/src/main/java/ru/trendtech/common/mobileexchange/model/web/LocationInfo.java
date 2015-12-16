package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 20.11.2014.
 */

public class LocationInfo {
    private String id;
    private long driverId;
    private long missionId;
    private double latitude;
    private double longitude;
    private Long when_seen;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Long getWhen_seen() {
        return when_seen;
    }

    public void setWhen_seen(Long when_seen) {
        this.when_seen = when_seen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
