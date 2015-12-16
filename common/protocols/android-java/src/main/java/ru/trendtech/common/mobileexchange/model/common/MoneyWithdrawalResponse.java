package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 07.10.2014.
 */

public class MoneyWithdrawalResponse {
    private String smsCode;
    private ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public ErrorCodeHelper getErrorCodeHelper() {
        return errorCodeHelper;
    }

    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
        this.errorCodeHelper = errorCodeHelper;
    }
}
