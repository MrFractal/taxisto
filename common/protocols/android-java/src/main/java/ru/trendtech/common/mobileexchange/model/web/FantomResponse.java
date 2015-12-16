package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.FantomInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 29.06.2015.
 */
public class FantomResponse extends ErrorCodeHelper {
    private List<FantomInfo> fantomInfos = new ArrayList<FantomInfo>();

    public List<FantomInfo> getFantomInfos() {
        return fantomInfos;
    }

    public void setFantomInfos(List<FantomInfo> fantomInfos) {
        this.fantomInfos = fantomInfos;
    }
}
