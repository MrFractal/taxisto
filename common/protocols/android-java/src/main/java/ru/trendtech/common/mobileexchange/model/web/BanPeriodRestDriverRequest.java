package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 28.01.2015.
 */
public class BanPeriodRestDriverRequest {
    private String security_token;
    private long banPeriodId;

    public long getBanPeriodId() {
        return banPeriodId;
    }

    public void setBanPeriodId(long banPeriodId) {
        this.banPeriodId = banPeriodId;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }
}
