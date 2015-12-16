package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.BanPeriodRestDriverInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 28.01.2015.
 */
public class BanPeriodRestDriverResponse {
    private List<BanPeriodRestDriverInfo> banPeriodRestDriverInfoList = new ArrayList<BanPeriodRestDriverInfo>();

    public List<BanPeriodRestDriverInfo> getBanPeriodRestDriverInfoList() {
        return banPeriodRestDriverInfoList;
    }

    public void setBanPeriodRestDriverInfoList(List<BanPeriodRestDriverInfo> banPeriodRestDriverInfoList) {
        this.banPeriodRestDriverInfoList = banPeriodRestDriverInfoList;
    }
}
