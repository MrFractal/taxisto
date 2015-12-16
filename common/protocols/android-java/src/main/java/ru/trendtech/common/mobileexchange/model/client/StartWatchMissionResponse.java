package ru.trendtech.common.mobileexchange.model.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.DriverInfoARM;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.ItemLocation;

/**
 * Created by petr on 08.12.2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StartWatchMissionResponse {
    private ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();
    private ItemLocation location;
    private DriverInfoARM driverInfoARM;
    private String stateMission;
    private double distanceBeforeArrival;
    private int minBeforeArrival;

    public String getStateMission() {
        return stateMission;
    }

    public void setStateMission(String stateMission) {
        this.stateMission = stateMission;
    }

    public double getDistanceBeforeArrival() {
        return distanceBeforeArrival;
    }

    public void setDistanceBeforeArrival(double distanceBeforeArrival) {
        this.distanceBeforeArrival = distanceBeforeArrival;
    }

    public int getMinBeforeArrival() {
        return minBeforeArrival;
    }

    public void setMinBeforeArrival(int minBeforeArrival) {
        this.minBeforeArrival = minBeforeArrival;
    }

    public DriverInfoARM getDriverInfoARM() {
        return driverInfoARM;
    }

    public void setDriverInfoARM(DriverInfoARM driverInfoARM) {
        this.driverInfoARM = driverInfoARM;
    }

    public ErrorCodeHelper getErrorCodeHelper() {
        return errorCodeHelper;
    }

    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
        this.errorCodeHelper = errorCodeHelper;
    }

    public ItemLocation getLocation() {
        return location;
    }

    public void setLocation(ItemLocation location) {
        this.location = location;
    }
}
