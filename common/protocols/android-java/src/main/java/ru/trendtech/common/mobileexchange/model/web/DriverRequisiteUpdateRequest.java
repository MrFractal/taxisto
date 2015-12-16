package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.DriverRequisiteInfo;

/**
 * Created by petr on 26.01.2015.
 */
public class DriverRequisiteUpdateRequest {
    private String security_token;
    private DriverRequisiteInfoARM driverRequisiteInfo;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public DriverRequisiteInfoARM getDriverRequisiteInfo() {
        return driverRequisiteInfo;
    }

    public void setDriverRequisiteInfo(DriverRequisiteInfoARM driverRequisiteInfo) {
        this.driverRequisiteInfo = driverRequisiteInfo;
    }
}
