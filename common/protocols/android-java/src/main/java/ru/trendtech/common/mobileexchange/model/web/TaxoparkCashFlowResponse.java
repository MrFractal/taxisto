package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.TaxoparkCashFlowInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 20.07.2015.
 */
public class TaxoparkCashFlowResponse extends ErrorCodeHelper {
    private List<TaxoparkCashFlowInfo> taxoparkCashFlowInfos = new ArrayList<TaxoparkCashFlowInfo>();
    private long lastPageNumber;
    private long totalItems;

    public List<TaxoparkCashFlowInfo> getTaxoparkCashFlowInfos() {
        return taxoparkCashFlowInfos;
    }

    public void setTaxoparkCashFlowInfos(List<TaxoparkCashFlowInfo> taxoparkCashFlowInfos) {
        this.taxoparkCashFlowInfos = taxoparkCashFlowInfos;
    }

    public long getLastPageNumber() {
        return lastPageNumber;
    }

    public void setLastPageNumber(long lastPageNumber) {
        this.lastPageNumber = lastPageNumber;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }
}
