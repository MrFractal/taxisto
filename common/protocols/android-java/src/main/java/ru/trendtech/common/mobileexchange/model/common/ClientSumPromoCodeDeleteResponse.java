package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 13.10.2014.
 */
public class ClientSumPromoCodeDeleteResponse {
    private ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();

    public ErrorCodeHelper getErrorCodeHelper() {
        return errorCodeHelper;
    }

    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
        this.errorCodeHelper = errorCodeHelper;
    }
}
