package ru.trendtech.common.mobileexchange.model.common.billing.paymobile;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 15.09.2014.
 */


public class StoreCardDataResponse {
    private boolean result =false;
    //private ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();

//    public ErrorCodeHelper getErrorCodeHelper() {
//        return errorCodeHelper;
//    }
//
//    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
//        this.errorCodeHelper = errorCodeHelper;
//    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
