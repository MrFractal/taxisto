package ru.trendtech.common.mobileexchange.model.driver;

import ru.trendtech.common.mobileexchange.model.courier.web.CommonRequest;

public class ReroutingRequest extends CommonRequest{
    private long missionId;
    private String address;
    private double latitude;
    private double longitude;

    public ReroutingRequest(long missionId, String address, double latitude, double longitude) {
        this.missionId = missionId;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
