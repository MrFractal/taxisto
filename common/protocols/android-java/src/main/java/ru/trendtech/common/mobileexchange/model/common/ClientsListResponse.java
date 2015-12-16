package ru.trendtech.common.mobileexchange.model.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 08.08.14.
 */
public class ClientsListResponse {
    private long lastPageNumber;
    private List<ClientInfoARM> clientInfo = new ArrayList<ClientInfoARM>();
    private Long totalItems;

    public Long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Long totalItems) {
        this.totalItems = totalItems;
    }

    public long getLastPageNumber() {
        return lastPageNumber;
    }

    public void setLastPageNumber(long lastPageNumber) {
        this.lastPageNumber = lastPageNumber;
    }

    public List<ClientInfoARM> getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(List<ClientInfoARM> clientInfo) {
        this.clientInfo = clientInfo;
    }
}
