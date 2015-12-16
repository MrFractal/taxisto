package ru.trendtech.common.mobileexchange.model.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.estimate.EstimateInfo;
import ru.trendtech.common.mobileexchange.model.web.DriverProfileInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 04.02.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverEstimatesV2Response {
    private List<EstimateInfo> estimateInfotList = new ArrayList<EstimateInfo>();
    private DriverProfileInfo driverProfileInfo;

    public List<EstimateInfo> getEstimateInfotList() {
        return estimateInfotList;
    }

    public void setEstimateInfotList(List<EstimateInfo> estimateInfotList) {
        this.estimateInfotList = estimateInfotList;
    }

    public DriverProfileInfo getDriverProfileInfo() {
        return driverProfileInfo;
    }

    public void setDriverProfileInfo(DriverProfileInfo driverProfileInfo) {
        this.driverProfileInfo = driverProfileInfo;
    }
}
