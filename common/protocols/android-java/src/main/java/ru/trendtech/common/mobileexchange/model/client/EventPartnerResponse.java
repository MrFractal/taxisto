package ru.trendtech.common.mobileexchange.model.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.EventPartnerInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 30.01.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventPartnerResponse extends ErrorCodeHelper{
    private List<EventPartnerInfo> eventPartnerInfos = new ArrayList<EventPartnerInfo>();

    public List<EventPartnerInfo> getEventPartnerInfos() {
        return eventPartnerInfos;
    }

    public void setEventPartnerInfos(List<EventPartnerInfo> eventPartnerInfos) {
        this.eventPartnerInfos = eventPartnerInfos;
    }
}
