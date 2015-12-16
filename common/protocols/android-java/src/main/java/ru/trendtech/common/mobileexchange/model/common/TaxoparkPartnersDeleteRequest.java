package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 14.10.2014.
 */
public class TaxoparkPartnersDeleteRequest {
    private long taxoparkId;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getTaxoparkId() {
        return taxoparkId;
    }

    public void setTaxoparkId(long taxoparkId) {
        this.taxoparkId = taxoparkId;
    }
}
