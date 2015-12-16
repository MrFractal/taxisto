package ru.trendtech.common.mobileexchange.model.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.DriverRequisiteInfo;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 27.01.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverRequisiteResponse extends ErrorCodeHelper{
    List<DriverRequisiteInfo> driverRequisiteInfos = new ArrayList<DriverRequisiteInfo>();

    public List<DriverRequisiteInfo> getDriverRequisiteInfos() {
        return driverRequisiteInfos;
    }

    public void setDriverRequisiteInfos(List<DriverRequisiteInfo> driverRequisiteInfos) {
        this.driverRequisiteInfos = driverRequisiteInfos;
    }
}
