package ru.trendtech.common.mobileexchange.model.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 06.02.14.
 */
public class DriversRatingsResponse {
    private List<RatingItem> ratings = new ArrayList<RatingItem>();

    public List<RatingItem> getRatings() {
        return ratings;
    }
}
