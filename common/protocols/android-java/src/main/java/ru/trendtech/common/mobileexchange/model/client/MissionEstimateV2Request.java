package ru.trendtech.common.mobileexchange.model.client;


import ru.trendtech.common.mobileexchange.model.common.estimate.EstimateInfo;

/**
 * Created by petr on 04.02.2015.
 */
public class MissionEstimateV2Request {
    private EstimateInfo estimateInfo;
    private String security_token;

    public EstimateInfo getEstimateInfo() {
        return estimateInfo;
    }

    public void setEstimateInfo(EstimateInfo estimateInfo) {
        this.estimateInfo = estimateInfo;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }
}
