package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ClientInfo;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 17.10.2014.
 */

public class ClientStatListResponse {
    private long lastPageNumber;
    private Long totalItems;
    private List<ClientStatsInfo_V2> clientInfoStat =new ArrayList<ClientStatsInfo_V2>();
    private ErrorCodeHelper errorCodeHelper =new ErrorCodeHelper();


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

    public List<ClientStatsInfo_V2> getClientInfoStat() {
        return clientInfoStat;
    }

    public void setClientInfoStat(List<ClientStatsInfo_V2> clientInfoStat) {
        this.clientInfoStat = clientInfoStat;
    }

    public ErrorCodeHelper getErrorCodeHelper() {
        return errorCodeHelper;
    }

    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
        this.errorCodeHelper = errorCodeHelper;
    }
}
