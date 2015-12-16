package ru.trendtech.common.mobileexchange.model.client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 05.11.2014.
 */
public class MarkMissionAsDeleteRequest {
    private long clientId;
    private String security_token;
    private List<Long> missionMarkAsDeleteIdList = new ArrayList<Long>();

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public List<Long> getMissionMarkAsDeleteIdList() {
        return missionMarkAsDeleteIdList;
    }

    public void setMissionMarkAsDeleteIdList(List<Long> missionMarkAsDeleteIdList) {
        this.missionMarkAsDeleteIdList = missionMarkAsDeleteIdList;
    }
}
