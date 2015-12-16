package ru.trendtech.common.mobileexchange.model.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.ItemLocation;
import ru.trendtech.common.mobileexchange.model.common.states.ServerStateInfo;

/**
 * Created by petr on 11.06.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientSystemConfigurationResponse extends ErrorCodeHelper {
    private ServerStateInfo serverStateInfo = new ServerStateInfo();
    private int howLongWaitDriverAssign = 0;
    private String useMap;// 1 - 2gis, 2 - yandex
    private ItemLocation driverCurrentLocation;
    private boolean isCorporate;
    private boolean isLate;
    private int afterMinBooked;

    public int getAfterMinBooked() {
        return afterMinBooked;
    }

    public void setAfterMinBooked(int afterMinBooked) {
        this.afterMinBooked = afterMinBooked;
    }

    public int getHowLongWaitDriverAssign() {
        return howLongWaitDriverAssign;
    }

    public void setHowLongWaitDriverAssign(int howLongWaitDriverAssign) {
        this.howLongWaitDriverAssign = howLongWaitDriverAssign;
    }

    public ServerStateInfo getServerStateInfo() {
        return serverStateInfo;
    }

    public void setServerStateInfo(ServerStateInfo serverStateInfo) {
        this.serverStateInfo = serverStateInfo;
    }

    public String getUseMap() {
        return useMap;
    }

    public void setUseMap(String useMap) {
        this.useMap = useMap;
    }

    public boolean isLate() {
        return isLate;
    }

    public void setLate(boolean isLate) {
        this.isLate = isLate;
    }

    public boolean isCorporate() {
        return isCorporate;
    }

    public void setCorporate(boolean isCorporate) {
        this.isCorporate = isCorporate;
    }

    public ItemLocation getDriverCurrentLocation() {
        return driverCurrentLocation;
    }

    public void setDriverCurrentLocation(ItemLocation driverCurrentLocation) {
        this.driverCurrentLocation = driverCurrentLocation;
    }
}
