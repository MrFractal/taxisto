package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 05.06.2015.
 */
public class TariffRestrictionResponse extends ErrorCodeHelper {
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
}
