package ru.trendtech.common.mobileexchange.model.common;

import ru.trendtech.common.mobileexchange.model.common.estimate.EstimateInfoARM;
import ru.trendtech.common.mobileexchange.model.common.scores.Scores;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 10.10.2014.
 */

public class EstimateInfoByClientResponse {
    private List<EstimateInfoARM> estimateInfoARMList = new ArrayList<EstimateInfoARM>();

    public List<EstimateInfoARM> getEstimateInfoARMList() {
        return estimateInfoARMList;
    }

    public void setEstimateInfoARMList(List<EstimateInfoARM> estimateInfoARMList) {
        this.estimateInfoARMList = estimateInfoARMList;
    }

}
