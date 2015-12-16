package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.DriverTimeWorkAndMissionCompleteInfo;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 19.05.2015.
 */
public class DriverTimeWorkAndMissionCompleteStatResponse extends ErrorCodeHelper {
    private List<DriverTimeWorkAndMissionCompleteInfo> driverTimeWorkAndMissionCompleteInfos = new ArrayList<DriverTimeWorkAndMissionCompleteInfo>();

    public List<DriverTimeWorkAndMissionCompleteInfo> getDriverTimeWorkAndMissionCompleteInfos() {
        return driverTimeWorkAndMissionCompleteInfos;
    }

    public void setDriverTimeWorkAndMissionCompleteInfos(List<DriverTimeWorkAndMissionCompleteInfo> driverTimeWorkAndMissionCompleteInfos) {
        this.driverTimeWorkAndMissionCompleteInfos = driverTimeWorkAndMissionCompleteInfos;
    }
}
