package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.states.ServerStateInfo;
import ru.trendtech.common.mobileexchange.model.common.support.SupportPhones;

/**
 * Created by petr on 11.06.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemConfiguration_V7_Response {
    private int howLongWaitDriverAssign = 0;
    private ServerStateInfo serverStateInfo = new ServerStateInfo();
    private String useMap;// 1 - 2gis, 2 - yandex
    private boolean isLate;
    private boolean isCorporate;
    private ItemLocation driverCurrentLocation;


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
