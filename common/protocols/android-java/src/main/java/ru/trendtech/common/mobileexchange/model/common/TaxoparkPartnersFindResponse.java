package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 14.10.2014.
 */
public class TaxoparkPartnersFindResponse {
    private TaxoparkPartnersInfo taxoparkPartnersInfo;
    private ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();

    public TaxoparkPartnersInfo getTaxoparkPartnersInfo() {
        return taxoparkPartnersInfo;
    }

    public void setTaxoparkPartnersInfo(TaxoparkPartnersInfo taxoparkPartnersInfo) {
        this.taxoparkPartnersInfo = taxoparkPartnersInfo;
    }

    public ErrorCodeHelper getErrorCodeHelper() {
        return errorCodeHelper;
    }

    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
        this.errorCodeHelper = errorCodeHelper;
    }
}
