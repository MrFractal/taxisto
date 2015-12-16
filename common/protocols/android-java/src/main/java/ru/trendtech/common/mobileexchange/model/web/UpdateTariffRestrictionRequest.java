package ru.trendtech.common.mobileexchange.model.web;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 05.06.2015.
 */
public class UpdateTariffRestrictionRequest {
    private String security_token;
    private List<TariffRestrictionInfo> tariffRestrictionInfos = new ArrayList<TariffRestrictionInfo>();
    private boolean off = false;

    public boolean isOff() {
        return off;
    }

    public void setOff(boolean off) {
        this.off = off;
    }

    public List<TariffRestrictionInfo> getTariffRestrictionInfos() {
        return tariffRestrictionInfos;
    }

    public void setTariffRestrictionInfos(List<TariffRestrictionInfo> tariffRestrictionInfos) {
        this.tariffRestrictionInfos = tariffRestrictionInfos;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }
}
