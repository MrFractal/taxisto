package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 11.02.2015.
 */
public class GlobalDriverStatsResponse extends ErrorCodeHelper {
    private List<GlobalDriverStatsInfo> globalDriverStatsInfos = new ArrayList<GlobalDriverStatsInfo>();

    public List<GlobalDriverStatsInfo> getGlobalDriverStatsInfos() {
        return globalDriverStatsInfos;
    }

    public void setGlobalDriverStatsInfos(List<GlobalDriverStatsInfo> globalDriverStatsInfos) {
        this.globalDriverStatsInfos = globalDriverStatsInfos;
    }
}
