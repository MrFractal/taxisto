package ru.trendtech.common.mobileexchange.model.node;

/**
 * Created by petr on 02.06.2015.
 */
public class FantomDriverRequest {
    private Long missionId;
    private Long clientId;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }
}
