package ru.trendtech.common.mobileexchange.model.web;

import java.util.ArrayList;
import java.util.List;

/**
 * File created by max on 21/06/2014 9:00.
 */


public class FindDriversRequest {
    private List<Long> ids = new ArrayList<Long>();

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
