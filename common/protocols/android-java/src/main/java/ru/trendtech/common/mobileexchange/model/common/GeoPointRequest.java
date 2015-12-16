package ru.trendtech.common.mobileexchange.model.common;

import ru.trendtech.common.mobileexchange.model.courier.web.CommonRequest;

/**
 * Created by petr on 28.08.2015.
 */
public class GeoPointRequest extends CommonRequest{
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
