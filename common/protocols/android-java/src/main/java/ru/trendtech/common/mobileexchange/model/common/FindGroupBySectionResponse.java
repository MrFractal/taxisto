package ru.trendtech.common.mobileexchange.model.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 19.09.2014.
 */

public class FindGroupBySectionResponse {
    private List<PartnersGroupInfo> partnersGroupInfoList = new ArrayList<PartnersGroupInfo>();

    public List<PartnersGroupInfo> getPartnersGroupInfoList() {
        return partnersGroupInfoList;
    }

    public void setPartnersGroupInfoList(List<PartnersGroupInfo> partnersGroupInfoList) {
        this.partnersGroupInfoList = partnersGroupInfoList;
    }
}
