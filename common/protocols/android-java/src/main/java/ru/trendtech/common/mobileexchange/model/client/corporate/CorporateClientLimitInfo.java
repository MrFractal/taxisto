package ru.trendtech.common.mobileexchange.model.client.corporate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 17.03.2015.
 */
public class CorporateClientLimitInfo {
    private long id;
    private long clientId;
    private long corporateAccountId;
    private long updateTime;
    private List<LimitInfo> limitInfos = new ArrayList<LimitInfo>();

    public List<LimitInfo> getLimitInfos() {
        return limitInfos;
    }

    public void setLimitInfos(List<LimitInfo> limitInfos) {
        this.limitInfos = limitInfos;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getCorporateAccountId() {
        return corporateAccountId;
    }

    public void setCorporateAccountId(long corporateAccountId) {
        this.corporateAccountId = corporateAccountId;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

}
