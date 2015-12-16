package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 06.10.2014.
 */
public class FindAddressRequest {
    private String security_token;
    private String addressMask;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public String getAddressMask() {
        return addressMask;
    }

    public void setAddressMask(String addressMask) {
        this.addressMask = addressMask;
    }
}
