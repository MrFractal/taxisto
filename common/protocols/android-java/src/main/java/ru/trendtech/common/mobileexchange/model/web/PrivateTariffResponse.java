package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.PrivateTariffInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 19.05.2015.
 */
public class PrivateTariffResponse extends ErrorCodeHelper {
    private List<PrivateTariffInfo> privateTariffInfos = new ArrayList<PrivateTariffInfo>();

    public List<PrivateTariffInfo> getPrivateTariffInfos() {
        return privateTariffInfos;
    }

    public void setPrivateTariffInfos(List<PrivateTariffInfo> privateTariffInfos) {
        this.privateTariffInfos = privateTariffInfos;
    }
}
