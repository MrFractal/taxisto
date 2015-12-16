package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.PartnersGroupInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 26.01.2015.
 */
public class FindGroupBySectionARMResponse {
    private List<PartnersGroupInfo> partnersGroupInfoList = new ArrayList<PartnersGroupInfo>();

    public List<PartnersGroupInfo> getPartnersGroupInfoList() {
        return partnersGroupInfoList;
    }

    public void setPartnersGroupInfoList(List<PartnersGroupInfo> partnersGroupInfoList) {
        this.partnersGroupInfoList = partnersGroupInfoList;
    }
}
