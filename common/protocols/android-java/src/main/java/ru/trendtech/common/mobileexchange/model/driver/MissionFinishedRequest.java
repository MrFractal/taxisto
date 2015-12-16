package ru.trendtech.common.mobileexchange.model.driver;

import ru.trendtech.common.mobileexchange.model.common.DriverRequest;
import ru.trendtech.common.mobileexchange.model.common.rates.ServicePriceInfo;

import java.util.List;

/**
 * Created by max on 13.02.14.
 */
public class MissionFinishedRequest extends DriverRequest {
    private long missionId;
    private int distanceInFact;
    private int pausesTime;
    private List<ServicePriceInfo> servicesInFact;

    public int getPausesTime() {
        return pausesTime;
    }

    public void setPausesTime(int pausesTime) {
        this.pausesTime = pausesTime;
    }

    public int getDistanceInFact() {
        return distanceInFact;
    }

    public void setDistanceInFact(int distanceInFact) {
        this.distanceInFact = distanceInFact;
    }

    public List<ServicePriceInfo> getServicesInFact() {
        return servicesInFact;
    }

    public void setServicesInFact(List<ServicePriceInfo> servicesInFact) {
        this.servicesInFact = servicesInFact;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }
}
