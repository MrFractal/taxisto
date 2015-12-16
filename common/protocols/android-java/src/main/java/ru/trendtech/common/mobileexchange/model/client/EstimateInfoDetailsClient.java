package ru.trendtech.common.mobileexchange.model.client;

import ru.trendtech.common.mobileexchange.model.common.DriverInfo;
import ru.trendtech.common.mobileexchange.model.common.estimate.EstimateInfoClient;

/**
 * Created by petr on 19.01.2015.
 */
public class EstimateInfoDetailsClient {
    private EstimateInfoClient estimateInfoClient;
    private DriverInfo driverInfo;
    private String driverDescription;

    public EstimateInfoClient getEstimateInfoClient() {
        return estimateInfoClient;
    }

    public void setEstimateInfoClient(EstimateInfoClient estimateInfoClient) {
        this.estimateInfoClient = estimateInfoClient;
    }

    public DriverInfo getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(DriverInfo driverInfo) {
        this.driverInfo = driverInfo;
    }

    public String getDriverDescription() {
        return driverDescription;
    }

    public void setDriverDescription(String driverDescription) {
        this.driverDescription = driverDescription;
    }
}
