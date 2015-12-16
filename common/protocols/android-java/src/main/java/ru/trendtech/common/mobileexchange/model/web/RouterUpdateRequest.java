package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.BaseRequest;
import ru.trendtech.common.mobileexchange.model.common.RouterInfo;
import ru.trendtech.common.mobileexchange.model.common.TabletInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 30.06.2015.
 */
public class RouterUpdateRequest extends BaseRequest {
    private List<RouterInfo> routerInfos = new ArrayList<RouterInfo>();

    public List<RouterInfo> getRouterInfos() {
        return routerInfos;
    }

    public void setRouterInfos(List<RouterInfo> routerInfos) {
        this.routerInfos = routerInfos;
    }
}
