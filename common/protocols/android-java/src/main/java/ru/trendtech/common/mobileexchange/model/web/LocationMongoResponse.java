package ru.trendtech.common.mobileexchange.model.web;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 20.11.2014.
 */
public class LocationMongoResponse {
    private List<LocationInfo> listLocation =  new ArrayList<LocationInfo>();
    private long totalItems;

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public List getListLocation() {
        return listLocation;
    }

    public void setListLocation(List listLocation) {
        this.listLocation = listLocation;
    }
}
