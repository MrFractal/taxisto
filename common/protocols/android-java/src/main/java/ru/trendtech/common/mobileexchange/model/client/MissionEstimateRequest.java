package ru.trendtech.common.mobileexchange.model.client;

import ru.trendtech.common.mobileexchange.model.common.estimate.EstimateInfoClient;

/**
 * Created by petr on 19.01.2015.
 */
public class MissionEstimateRequest {
    private EstimateInfoClient estimateInfoClient;
    private String security_token;

    public EstimateInfoClient getEstimateInfoClient() {
        return estimateInfoClient;
    }

    public void setEstimateInfoClient(EstimateInfoClient estimateInfoClient) {
        this.estimateInfoClient = estimateInfoClient;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }
}
