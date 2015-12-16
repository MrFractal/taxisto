package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.EventPartnerInfo;
import ru.trendtech.common.mobileexchange.model.common.MissionInfoARM;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 30.01.2015.
 */
public class EventPartnerARMResponse extends ErrorCodeHelper{
    private long lastPageNumber;
    private long totalItems;
    private List<EventPartnerInfo> eventPartnerInfos = new ArrayList<EventPartnerInfo>();

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

    public List<EventPartnerInfo> getEventPartnerInfos() {
        return eventPartnerInfos;
    }

    public void setEventPartnerInfos(List<EventPartnerInfo> eventPartnerInfos) {
        this.eventPartnerInfos = eventPartnerInfos;
    }
}
