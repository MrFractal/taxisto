package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 26.04.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverRequisiteResponseV2 extends ErrorCodeHelper {
    private List<DriverRequisiteInfoV2> driverRequisiteInfos = new ArrayList<DriverRequisiteInfoV2>();

    public List<DriverRequisiteInfoV2> getDriverRequisiteInfos() {
        return driverRequisiteInfos;
    }

    public void setDriverRequisiteInfos(List<DriverRequisiteInfoV2> driverRequisiteInfos) {
        this.driverRequisiteInfos = driverRequisiteInfos;
    }
}
