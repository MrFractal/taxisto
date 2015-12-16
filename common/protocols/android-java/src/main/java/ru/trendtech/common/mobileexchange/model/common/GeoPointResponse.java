package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 28.08.2015.
 */
public class GeoPointResponse extends ErrorCodeHelper {
    private String point; // format: 83.094057 54.764479

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
