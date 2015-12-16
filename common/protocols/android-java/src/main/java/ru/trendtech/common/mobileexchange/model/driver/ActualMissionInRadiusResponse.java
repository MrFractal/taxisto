package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 21.01.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActualMissionInRadiusResponse  extends ErrorCodeHelper{
    private List<DistanceDetails> distanceDetailsList = new ArrayList();

    public List<DistanceDetails> getDistanceDetailsList() {
        return distanceDetailsList;
    }

    public void setDistanceDetailsList(List<DistanceDetails> distanceDetailsList) {
        this.distanceDetailsList = distanceDetailsList;
    }
}
