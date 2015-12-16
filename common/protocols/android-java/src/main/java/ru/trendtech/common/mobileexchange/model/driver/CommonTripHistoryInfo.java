package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 28.10.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonTripHistoryInfo {
    private long tripId; // orderId or missionId
    private String typeTrip = "";
    private String dateTrip = "";
    private String timeTrip = "";
    private String address = "";
    private String priceInFact = "";
    private String increaseAmount = "";

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public String getTypeTrip() {
        return typeTrip;
    }

    public void setTypeTrip(String typeTrip) {
        this.typeTrip = typeTrip;
    }

    public String getDateTrip() {
        return dateTrip;
    }

    public void setDateTrip(String dateTrip) {
        this.dateTrip = dateTrip;
    }

    public String getTimeTrip() {
        return timeTrip;
    }

    public void setTimeTrip(String timeTrip) {
        this.timeTrip = timeTrip;
    }

    public String getPriceInFact() {
        return priceInFact;
    }

    public void setPriceInFact(String priceInFact) {
        this.priceInFact = priceInFact;
    }

    public String getIncreaseAmount() {
        return increaseAmount;
    }

    public void setIncreaseAmount(String increaseAmount) {
        this.increaseAmount = increaseAmount;
    }
}
