package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.estimate.EstimateInfoARM;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 20.01.2015.
 */
public class UpdateEstimateRequest {
    private String security_token;
    private List<EstimateInfoARM> estimateInfoARMList = new ArrayList<EstimateInfoARM>();

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public List<EstimateInfoARM> getEstimateInfoARMList() {
        return estimateInfoARMList;
    }

    public void setEstimateInfoARMList(List<EstimateInfoARM> estimateInfoARMList) {
        this.estimateInfoARMList = estimateInfoARMList;
    }
}
