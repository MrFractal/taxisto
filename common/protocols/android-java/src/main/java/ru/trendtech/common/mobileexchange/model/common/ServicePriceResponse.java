package ru.trendtech.common.mobileexchange.model.common;

import ru.trendtech.common.mobileexchange.model.common.rates.ServicePriceInfo;
import ru.trendtech.common.mobileexchange.model.common.rates.ServicePriceInfoV2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 11.06.2015.
 */
public class ServicePriceResponse extends ErrorCodeHelper {
    private List<ServicePriceInfo> servicePriceInfos = new ArrayList<ServicePriceInfo>();

    public List<ServicePriceInfo> getServicePriceInfos() {
        return servicePriceInfos;
    }

    public void setServicePriceInfos(List<ServicePriceInfo> servicePriceInfos) {
        this.servicePriceInfos = servicePriceInfos;
    }
}
