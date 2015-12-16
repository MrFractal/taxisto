package ru.trendtech.common.mobileexchange.model.common.billing.paymobile;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 17.10.2014.
 */

public class PayTestForRegisterCardResponse {
    private String mdOrder;
    private String formUrl;
    private String errorMessage;
    private String errorCode;
    private ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();

    public ErrorCodeHelper getErrorCodeHelper() {
        return errorCodeHelper;
    }

    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
        this.errorCodeHelper = errorCodeHelper;
    }

    public String getMdOrder() {
        return mdOrder;
    }

    public void setMdOrder(String mdOrder) {
        this.mdOrder = mdOrder;
    }

    public String getFormUrl() {
        return formUrl;
    }

    public void setFormUrl(String formUrl) {
        this.formUrl = formUrl;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
