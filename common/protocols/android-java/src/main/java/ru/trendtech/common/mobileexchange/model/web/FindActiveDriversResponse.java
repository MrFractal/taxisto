package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ItemLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * File created by max on 09/06/2014 23:28.
 */


public class FindActiveDriversResponse {
    private List<ItemLocation> locations = new ArrayList<ItemLocation>();

    public List<ItemLocation> getLocations() {
        return locations;
    }
}
