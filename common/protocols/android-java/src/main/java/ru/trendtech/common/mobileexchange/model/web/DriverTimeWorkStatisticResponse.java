package ru.trendtech.common.mobileexchange.model.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.DriverTimeWorkInfo;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 14.05.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverTimeWorkStatisticResponse extends ErrorCodeHelper {
    private List<DriverTimeWorkInfo> driverTimeWorkInfos = new ArrayList<DriverTimeWorkInfo>();

    public List<DriverTimeWorkInfo> getDriverTimeWorkInfos() {
        return driverTimeWorkInfos;
    }

    public void setDriverTimeWorkInfos(List<DriverTimeWorkInfo> driverTimeWorkInfos) {
        this.driverTimeWorkInfos = driverTimeWorkInfos;
    }
}
