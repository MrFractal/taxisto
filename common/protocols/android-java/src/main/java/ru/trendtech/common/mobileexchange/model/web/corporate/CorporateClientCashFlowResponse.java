package ru.trendtech.common.mobileexchange.model.web.corporate;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.corporate.CorporateClientCashFlowInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 07.04.2015.
 */
public class CorporateClientCashFlowResponse extends ErrorCodeHelper {
    private List<CorporateClientCashFlowInfo> corporateClientCashFlowInfos = new ArrayList<CorporateClientCashFlowInfo>();
    private long lastPageNumber;
    private Long totalItems;

    public long getLastPageNumber() {
        return lastPageNumber;
    }

    public void setLastPageNumber(long lastPageNumber) {
        this.lastPageNumber = lastPageNumber;
    }

    public Long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Long totalItems) {
        this.totalItems = totalItems;
    }

    public List<CorporateClientCashFlowInfo> getCorporateClientCashFlowInfos() {
        return corporateClientCashFlowInfos;
    }

    public void setCorporateClientCashFlowInfos(List<CorporateClientCashFlowInfo> corporateClientCashFlowInfos) {
        this.corporateClientCashFlowInfos = corporateClientCashFlowInfos;
    }
}
