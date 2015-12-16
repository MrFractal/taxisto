package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.EventPartnerInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 29.01.2015.
 */
public class UpdateEventPartnerRequest {
    private String security_token;
    private List<EventPartnerInfo> eventPartnerInfos = new ArrayList<EventPartnerInfo>();

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public List<EventPartnerInfo> getEventPartnerInfos() {
        return eventPartnerInfos;
    }

    public void setEventPartnerInfos(List<EventPartnerInfo> eventPartnerInfos) {
        this.eventPartnerInfos = eventPartnerInfos;
    }
}
