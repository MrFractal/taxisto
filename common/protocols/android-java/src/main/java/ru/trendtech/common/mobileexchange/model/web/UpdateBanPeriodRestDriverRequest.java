package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.BanPeriodRestDriverInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 28.01.2015.
 */

public class UpdateBanPeriodRestDriverRequest {
    private String security_token;
    private List<BanPeriodRestDriverInfo> banPeriodRestDriverInfoList = new ArrayList<BanPeriodRestDriverInfo>();

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public List<BanPeriodRestDriverInfo> getBanPeriodRestDriverInfoList() {
        return banPeriodRestDriverInfoList;
    }

    public void setBanPeriodRestDriverInfoList(List<BanPeriodRestDriverInfo> banPeriodRestDriverInfoList) {
        this.banPeriodRestDriverInfoList = banPeriodRestDriverInfoList;
    }
}
