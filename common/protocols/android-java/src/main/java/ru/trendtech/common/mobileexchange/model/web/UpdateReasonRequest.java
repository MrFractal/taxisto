package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ReasonInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 15.07.2015.
 */
public class UpdateReasonRequest {
    private String security_token;
    private List<ReasonInfo> reasonInfos = new ArrayList<ReasonInfo>();

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public List<ReasonInfo> getReasonInfos() {
        return reasonInfos;
    }

    public void setReasonInfos(List<ReasonInfo> reasonInfos) {
        this.reasonInfos = reasonInfos;
    }
}
