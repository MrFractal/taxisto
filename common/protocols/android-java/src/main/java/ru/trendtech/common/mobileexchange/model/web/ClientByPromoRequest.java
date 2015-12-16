package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 10.12.2014.
 */
public class ClientByPromoRequest {
    private String promoCode;
    private String security_token;

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }
}
