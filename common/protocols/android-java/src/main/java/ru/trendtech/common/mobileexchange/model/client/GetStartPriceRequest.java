package ru.trendtech.common.mobileexchange.model.client;

import java.util.ArrayList;
import java.util.List;

public class GetStartPriceRequest {
    private int auto_class;
    private long distance;
    private List<Integer> options = new ArrayList<Integer>();

    public List<Integer> getOptions() {
        return options;
    }

    public void setOptions(List<Integer> options) {
        this.options = options;
    }

    public int getAuto_class() {
        return auto_class;
    }

    public void setAuto_class(int auto_class) {
        this.auto_class = auto_class;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }
}
