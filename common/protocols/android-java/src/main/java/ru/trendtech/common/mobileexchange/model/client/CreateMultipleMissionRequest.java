package ru.trendtech.common.mobileexchange.model.client;

import ru.trendtech.common.mobileexchange.model.common.MultipleMissionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 29.04.2015.
 */
public class CreateMultipleMissionRequest {
    private String security_token;
    private long clientId;
    private List<MultipleMissionInfo> multipleMissionInfos = new ArrayList<MultipleMissionInfo>();

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public List<MultipleMissionInfo> getMultipleMissionInfos() {
        return multipleMissionInfos;
    }

    public void setMultipleMissionInfos(List<MultipleMissionInfo> multipleMissionInfos) {
        this.multipleMissionInfos = multipleMissionInfos;
    }
}
