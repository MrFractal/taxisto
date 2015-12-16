package ru.trendtech.common.mobileexchange.model.client;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 06.10.2014.
 */
public class FindAddressResponse {
    private List dgisStreetList = new ArrayList();
    private ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();

    public List getDgisStreetList() {
        return dgisStreetList;
    }

    public void setDgisStreetList(List dgisStreetList) {
        this.dgisStreetList = dgisStreetList;
    }

    public ErrorCodeHelper getErrorCodeHelper() {
        return errorCodeHelper;
    }

    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
        this.errorCodeHelper = errorCodeHelper;
    }
}
