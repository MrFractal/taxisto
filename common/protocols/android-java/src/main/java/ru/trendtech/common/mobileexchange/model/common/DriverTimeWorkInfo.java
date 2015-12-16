package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 14.05.2015.
 */
public class DriverTimeWorkInfo {
    private String sumStayBusy;
    private String sumStateOnline;
    private String sumTimeSecPayRest;
    private String sumTimeSecRest;
    private String sumTimeSecWork;
    private DriverInfo driverInfo;

    public DriverInfo getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(DriverInfo driverInfo) {
        this.driverInfo = driverInfo;
    }

    public String getSumStayBusy() {
        return sumStayBusy;
    }

    public void setSumStayBusy(String sumStayBusy) {
        this.sumStayBusy = sumStayBusy;
    }

    public String getSumStateOnline() {
        return sumStateOnline;
    }

    public void setSumStateOnline(String sumStateOnline) {
        this.sumStateOnline = sumStateOnline;
    }

    public String getSumTimeSecPayRest() {
        return sumTimeSecPayRest;
    }

    public void setSumTimeSecPayRest(String sumTimeSecPayRest) {
        this.sumTimeSecPayRest = sumTimeSecPayRest;
    }

    public String getSumTimeSecRest() {
        return sumTimeSecRest;
    }

    public void setSumTimeSecRest(String sumTimeSecRest) {
        this.sumTimeSecRest = sumTimeSecRest;
    }

    public String getSumTimeSecWork() {
        return sumTimeSecWork;
    }

    public void setSumTimeSecWork(String sumTimeSecWork) {
        this.sumTimeSecWork = sumTimeSecWork;
    }
}
