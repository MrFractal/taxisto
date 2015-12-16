package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 20.11.2014.
 */

public class LocationMongoRequest {
    private int latStart;
    private int latEnd;
    private int lonStart;
    private int lonEnd;
    private int numberPage;
    private int sizePage;
    private long missionId;
    private long driverId;
    private long startWhenSeen;
    private long endWhenSeen;
    private String type; // ((going_to_client, going_with_client, stop_with_client, trouble, free))

    public int getLatStart() {
        return latStart;
    }

    public void setLatStart(int latStart) {
        this.latStart = latStart;
    }

    public int getLatEnd() {
        return latEnd;
    }

    public void setLatEnd(int latEnd) {
        this.latEnd = latEnd;
    }

    public int getLonStart() {
        return lonStart;
    }

    public void setLonStart(int lonStart) {
        this.lonStart = lonStart;
    }

    public int getLonEnd() {
        return lonEnd;
    }

    public void setLonEnd(int lonEnd) {
        this.lonEnd = lonEnd;
    }

    public int getNumberPage() {
        return numberPage;
    }

    public void setNumberPage(int numberPage) {
        this.numberPage = numberPage;
    }

    public int getSizePage() {
        return sizePage;
    }

    public void setSizePage(int sizePage) {
        this.sizePage = sizePage;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public long getStartWhenSeen() {
        return startWhenSeen;
    }

    public void setStartWhenSeen(long startWhenSeen) {
        this.startWhenSeen = startWhenSeen;
    }

    public long getEndWhenSeen() {
        return endWhenSeen;
    }

    public void setEndWhenSeen(long endWhenSeen) {
        this.endWhenSeen = endWhenSeen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }
}
