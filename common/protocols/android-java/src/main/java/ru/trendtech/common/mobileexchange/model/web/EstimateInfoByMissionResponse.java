package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.EstimateInfoDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 16.10.2014.
 */
public class EstimateInfoByMissionResponse {
    private EstimateInfoDetails estimateInfoDetails;
    private ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();

    public EstimateInfoDetails getEstimateInfoDetails() {
        return estimateInfoDetails;
    }

    public void setEstimateInfoDetails(EstimateInfoDetails estimateInfoDetails) {
        this.estimateInfoDetails = estimateInfoDetails;
    }

    public ErrorCodeHelper getErrorCodeHelper() {
        return errorCodeHelper;
    }

    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
        this.errorCodeHelper = errorCodeHelper;
    }
}
