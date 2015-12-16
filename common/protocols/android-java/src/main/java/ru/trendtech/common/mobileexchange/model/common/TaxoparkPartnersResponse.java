package ru.trendtech.common.mobileexchange.model.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 13.10.2014.
 */
public class TaxoparkPartnersResponse {
    private List<TaxoparkPartnersInfo> taxoparkPartnersInfoList = new ArrayList<TaxoparkPartnersInfo>();

    public List<TaxoparkPartnersInfo> getTaxoparkPartnersInfoList() {
        return taxoparkPartnersInfoList;
    }

    public void setTaxoparkPartnersInfoList(List<TaxoparkPartnersInfo> taxoparkPartnersInfoList) {
        this.taxoparkPartnersInfoList = taxoparkPartnersInfoList;
    }
}
