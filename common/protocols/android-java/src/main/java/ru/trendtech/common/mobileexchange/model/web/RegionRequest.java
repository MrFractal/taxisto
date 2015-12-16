package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 22.04.2015.
 */
public class RegionRequest {
    private String security_token;
    private long regionId;
    private String coast;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getRegionId() {
        return regionId;
    }

    public void setRegionId(long regionId) {
        this.regionId = regionId;
    }

    public String getCoast() {
        return coast;
    }

    public void setCoast(String coast) {
        this.coast = coast;
    }
}
