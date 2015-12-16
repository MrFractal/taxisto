package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.FantomInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 29.06.2015.
 */
public class FantomUpdateRequest {
    private String security_token;
    private List<FantomInfo> fantomInfos = new ArrayList<FantomInfo>();

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public List<FantomInfo> getFantomInfos() {
        return fantomInfos;
    }

    public void setFantomInfos(List<FantomInfo> fantomInfos) {
        this.fantomInfos = fantomInfos;
    }
}
