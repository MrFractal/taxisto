package ru.trendtech.common.mobileexchange.model.client;

import ru.trendtech.common.mobileexchange.model.common.DriverRequest;

/**
 * Created by max on 06.02.14.
 */
public class AssignedDriverRequest extends DriverRequest{
    private long missionId;
    private String security_token;

    public AssignedDriverRequest() {
        super();
    }

    public AssignedDriverRequest(long driverId, long missionId) {
        super(driverId);
        this.missionId = missionId;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }
}
