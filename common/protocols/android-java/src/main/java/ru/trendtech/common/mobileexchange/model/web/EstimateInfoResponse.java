package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.estimate.EstimateInfoARM;
import ru.trendtech.common.mobileexchange.model.common.estimate.EstimateInfoClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 24.10.2014.
 */
public class EstimateInfoResponse {
    private List<EstimateInfoARM> estimateInfoARMList = new ArrayList<EstimateInfoARM>(); // EstimateInfoDetails
    private long totalItems;

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public List<EstimateInfoARM> getEstimateInfoARMList() {
        return estimateInfoARMList;
    }

    public void setEstimateInfoARMList(List<EstimateInfoARM> estimateInfoARMList) {
        this.estimateInfoARMList = estimateInfoARMList;
    }
}
