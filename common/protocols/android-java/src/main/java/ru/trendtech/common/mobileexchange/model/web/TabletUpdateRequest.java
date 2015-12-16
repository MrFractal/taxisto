package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.TabletInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 22.06.2015.
 */
public class TabletUpdateRequest {
    private String security_token;
    private List<TabletInfo> tabletInfos = new ArrayList<TabletInfo>();

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public List<TabletInfo> getTabletInfos() {
        return tabletInfos;
    }

    public void setTabletInfos(List<TabletInfo> tabletInfos) {
        this.tabletInfos = tabletInfos;
    }
}
