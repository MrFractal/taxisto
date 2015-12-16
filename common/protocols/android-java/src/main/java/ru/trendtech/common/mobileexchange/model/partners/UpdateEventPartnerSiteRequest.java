package ru.trendtech.common.mobileexchange.model.partners;

import ru.trendtech.common.mobileexchange.model.common.EventPartnerSiteInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 24.03.2015.
 */
public class UpdateEventPartnerSiteRequest {
    private String security_token;
    private List<EventPartnerSiteInfo> eventPartnerInfos = new ArrayList<EventPartnerSiteInfo>();

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public List<EventPartnerSiteInfo> getEventPartnerInfos() {
        return eventPartnerInfos;
    }

    public void setEventPartnerInfos(List<EventPartnerSiteInfo> eventPartnerInfos) {
        this.eventPartnerInfos = eventPartnerInfos;
    }
}
