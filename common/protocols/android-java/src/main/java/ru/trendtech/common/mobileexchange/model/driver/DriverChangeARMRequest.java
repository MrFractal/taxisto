package ru.trendtech.common.mobileexchange.model.driver;

import ru.trendtech.common.mobileexchange.model.common.DriverInfoARM;

/**
 * Created by petr on 14.10.2014.
 */
public class DriverChangeARMRequest {
    private DriverInfoARM driverInfoARM;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public DriverInfoARM getDriverInfoARM() {
        return driverInfoARM;
    }

    public void setDriverInfoARM(DriverInfoARM driverInfoARM) {
        this.driverInfoARM = driverInfoARM;
    }
}
