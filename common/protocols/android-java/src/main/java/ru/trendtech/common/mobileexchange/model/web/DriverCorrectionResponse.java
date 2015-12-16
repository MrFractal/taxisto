package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.DriverCorrectionInfo;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 20.03.2015.
 */
public class DriverCorrectionResponse extends ErrorCodeHelper {
     private List<DriverCorrectionInfo> driverCorrectionInfos = new ArrayList<DriverCorrectionInfo>();

    public List<DriverCorrectionInfo> getDriverCorrectionInfos() {
        return driverCorrectionInfos;
    }

    public void setDriverCorrectionInfos(List<DriverCorrectionInfo> driverCorrectionInfos) {
        this.driverCorrectionInfos = driverCorrectionInfos;
    }
}
