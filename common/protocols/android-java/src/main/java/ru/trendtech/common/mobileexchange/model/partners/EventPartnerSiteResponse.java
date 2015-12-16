package ru.trendtech.common.mobileexchange.model.partners;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.EventPartnerSiteInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 24.03.2015.
 */
public class EventPartnerSiteResponse extends ErrorCodeHelper {
    private List<EventPartnerSiteInfo> eventList = new ArrayList<EventPartnerSiteInfo>();
    private long lastPageNumber;
    private long totalItems;

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

    public List<EventPartnerSiteInfo> getEventList() {
        return eventList;
    }

    public void setEventList(List<EventPartnerSiteInfo> eventList) {
        this.eventList = eventList;
    }


}
