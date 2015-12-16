package ru.trendtech.common.mobileexchange.model.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 29.12.2014.
 */
public class BonusSumAmountResponse {
    private ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();
    private List<Long> driverIdList = new ArrayList<Long>();

    public List<Long> getDriverIdList() {
        return driverIdList;
    }

    public void setDriverIdList(List<Long> driverIdList) {
        this.driverIdList = driverIdList;
    }

    public ErrorCodeHelper getErrorCodeHelper() {
        return errorCodeHelper;
    }

    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
        this.errorCodeHelper = errorCodeHelper;
    }
}
