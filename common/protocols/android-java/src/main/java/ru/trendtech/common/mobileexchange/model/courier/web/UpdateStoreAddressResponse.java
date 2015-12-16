package ru.trendtech.common.mobileexchange.model.courier.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.courier.StoreAddressInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 26.08.2015.
 */
public class UpdateStoreAddressResponse extends ErrorCodeHelper {
    private List<StoreAddressInfo> storeAddressInfos = new ArrayList<StoreAddressInfo>();

    public List<StoreAddressInfo> getStoreAddressInfos() {
        return storeAddressInfos;
    }

    public void setStoreAddressInfos(List<StoreAddressInfo> storeAddressInfos) {
        this.storeAddressInfos = storeAddressInfos;
    }
}
