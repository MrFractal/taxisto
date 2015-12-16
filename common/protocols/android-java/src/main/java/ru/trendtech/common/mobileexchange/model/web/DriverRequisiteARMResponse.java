package ru.trendtech.common.mobileexchange.model.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 26.03.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverRequisiteARMResponse {
    private List<DriverRequisiteInfoARM> driverRequisiteInfos = new ArrayList<DriverRequisiteInfoARM>();

    public List<DriverRequisiteInfoARM> getDriverRequisiteInfos() {
        return driverRequisiteInfos;
    }

    public void setDriverRequisiteInfos(List<DriverRequisiteInfoARM> driverRequisiteInfos) {
        this.driverRequisiteInfos = driverRequisiteInfos;
    }
}
