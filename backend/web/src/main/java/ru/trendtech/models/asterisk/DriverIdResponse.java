package ru.trendtech.models.asterisk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivanenok on 4/13/2014.
 */
public class DriverIdResponse extends AsteriskResponse {
    private List<String> driversIds = new ArrayList<>();

    public DriverIdResponse() {
    }

    public DriverIdResponse(AsteriskRequest request) {
        super(request);
    }

    public List<String> getDriversIds() {
        return driversIds;
    }

    public void setDriversIds(List<String> driversIds) {
        this.driversIds = driversIds;
    }
}
