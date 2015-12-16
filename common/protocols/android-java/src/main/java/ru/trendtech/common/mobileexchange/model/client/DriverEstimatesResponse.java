package ru.trendtech.common.mobileexchange.model.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.DriverInfo;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.estimate.EstimateInfoClient;
import ru.trendtech.common.mobileexchange.model.web.DriverProfileInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 19.01.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverEstimatesResponse {
   private List<EstimateInfoClient> estimateInfoClientList = new ArrayList<EstimateInfoClient>();
   private DriverProfileInfo driverProfileInfo;

    public List<EstimateInfoClient> getEstimateInfoClientList() {
        return estimateInfoClientList;
    }

    public void setEstimateInfoClientList(List<EstimateInfoClient> estimateInfoClientList) {
        this.estimateInfoClientList = estimateInfoClientList;
    }

    public DriverProfileInfo getDriverProfileInfo() {
        return driverProfileInfo;
    }

    public void setDriverProfileInfo(DriverProfileInfo driverProfileInfo) {
        this.driverProfileInfo = driverProfileInfo;
    }
}
