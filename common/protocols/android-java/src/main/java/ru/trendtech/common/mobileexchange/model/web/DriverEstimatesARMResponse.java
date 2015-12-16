package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.estimate.EstimateInfoARM;
import ru.trendtech.common.mobileexchange.model.common.estimate.EstimateInfoClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 20.01.2015.
 */
public class DriverEstimatesARMResponse {
    private List<EstimateInfoARM> estimateInfoClientList = new ArrayList<EstimateInfoARM>();

    public List<EstimateInfoARM> getEstimateInfoClientList() {
        return estimateInfoClientList;
    }

    public void setEstimateInfoClientList(List<EstimateInfoARM> estimateInfoClientList) {
        this.estimateInfoClientList = estimateInfoClientList;
    }
}
