package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 13.02.2015.
 */
public class GlobalClientStatsResponse extends ErrorCodeHelper{
    private List<GlobalClientStatsInfo> globalClientStatsInfos = new ArrayList<GlobalClientStatsInfo>();

    public List<GlobalClientStatsInfo> getGlobalClientStatsInfos() {
        return globalClientStatsInfos;
    }

    public void setGlobalClientStatsInfos(List<GlobalClientStatsInfo> globalClientStatsInfos) {
        this.globalClientStatsInfos = globalClientStatsInfos;
    }
}
