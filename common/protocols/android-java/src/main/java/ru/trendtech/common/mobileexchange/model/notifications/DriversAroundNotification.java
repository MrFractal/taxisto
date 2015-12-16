package ru.trendtech.common.mobileexchange.model.notifications;

import ru.trendtech.common.mobileexchange.model.common.ItemLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 06.02.14.
 */
public class DriversAroundNotification {
    private List<ItemLocation> locations = new ArrayList<ItemLocation>();

    public List<ItemLocation> getLocations() {
        return locations;
    }
}
