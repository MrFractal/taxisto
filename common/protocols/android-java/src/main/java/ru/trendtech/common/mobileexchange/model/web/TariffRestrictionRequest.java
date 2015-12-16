package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 05.06.2015.
 */
public class TariffRestrictionRequest {
    private String security_token;
    private boolean holiday = false;


    public boolean isHoliday() {
        return holiday;
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }
}
