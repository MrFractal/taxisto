package ru.trendtech.common.mobileexchange.model.common;

/**
 * File created by max on 06/05/2014 21:12.
 */


public class ActivateBonusCardResponse extends DumbResponse {
    private double amount = 0;
    //private ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();

//    public ErrorCodeHelper getErrorCodeHelper() {
//        return errorCodeHelper;
//    }
//
//    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
//        this.errorCodeHelper = errorCodeHelper;
//    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
