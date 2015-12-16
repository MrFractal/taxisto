package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.AdditionalServiceInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 07.07.2015.
 */
public class UpdateAdditionalServiceRequest {
    private String security_token;
    private List<AdditionalServiceInfo> additionalServiceInfoList = new ArrayList<AdditionalServiceInfo>();

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public List<AdditionalServiceInfo> getAdditionalServiceInfoList() {
        return additionalServiceInfoList;
    }

    public void setAdditionalServiceInfoList(List<AdditionalServiceInfo> additionalServiceInfoList) {
        this.additionalServiceInfoList = additionalServiceInfoList;
    }
}
