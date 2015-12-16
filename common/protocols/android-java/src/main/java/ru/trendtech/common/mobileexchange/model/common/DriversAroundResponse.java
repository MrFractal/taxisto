package ru.trendtech.common.mobileexchange.model.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 06.02.14.
 */
public class DriversAroundResponse {
    private List<ItemLocation> locations = new ArrayList<ItemLocation>();

    public List<ItemLocation> getLocations() {
        return locations;
    }
}
