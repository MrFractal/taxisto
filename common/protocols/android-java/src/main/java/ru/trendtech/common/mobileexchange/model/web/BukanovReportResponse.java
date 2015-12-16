package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 06.08.2015.
 */
public class BukanovReportResponse extends ErrorCodeHelper {
    private BukanovReportInfo reportInfo = new BukanovReportInfo();

    public BukanovReportInfo getReportInfo() {
        return reportInfo;
    }

    public void setReportInfo(BukanovReportInfo reportInfo) {
        this.reportInfo = reportInfo;
    }
}
