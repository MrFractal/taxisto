package ru.trendtech.common.mobileexchange.model.courier.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.courier.DefaultPriceInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 28.08.2015.
 */
public class DefaultPriceResponse extends ErrorCodeHelper {
    private List<DefaultPriceInfo> defaultPriceInfoList = new ArrayList<DefaultPriceInfo>();

    public List<DefaultPriceInfo> getDefaultPriceInfoList() {
        return defaultPriceInfoList;
    }

    public void setDefaultPriceInfoList(List<DefaultPriceInfo> defaultPriceInfoList) {
        this.defaultPriceInfoList = defaultPriceInfoList;
    }
}
