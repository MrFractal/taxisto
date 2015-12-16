package ru.trendtech.common.mobileexchange.model.client;

import ru.trendtech.common.mobileexchange.model.common.ItemLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 06.02.14.
 */

public class AssignedDriverLocationResponse {
    private ItemLocation location;

    private boolean arrived = false;

    private List<Integer> arrivalTimes = new ArrayList<Integer>();

    public ItemLocation getLocation() {
        return location;
    }

    public void setLocation(ItemLocation location) {
        this.location = location;
    }

    public boolean isArrived() {
        return arrived;
    }

    public void setArrived(boolean arrived) {
        this.arrived = arrived;
    }

    public List<Integer> getArrivalTimes() {
        return arrivalTimes;
    }
}
