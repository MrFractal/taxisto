package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.rates.MissionRateInfoV2;
import ru.trendtech.common.mobileexchange.model.common.states.ServerStateInfo;
import ru.trendtech.common.mobileexchange.model.common.states.ServerStateInfoV2;
import ru.trendtech.common.mobileexchange.model.common.support.SupportPhones;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 15.05.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemConfiguration_V6_Response {
    private List<MissionRateInfoV2> rates = new ArrayList<MissionRateInfoV2>();
    private ServerStateInfoV2 serverStateInfo = new ServerStateInfoV2();
    private SupportPhones supportPhones = new SupportPhones();
    private String useMap;// 1 - 2gis, 2 - yandex
    private ItemLocation driverCurrentLocation;
    private int howLongWaitDriverAssign = 0;
    private boolean isCorporate;
    private boolean isLate;


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

    public List<ru.trendtech.common.mobileexchange.model.common.rates.MissionRateInfoV2> getRates() {
        return rates;
    }

    public void setRates(List<ru.trendtech.common.mobileexchange.model.common.rates.MissionRateInfoV2> rates) {
        this.rates = rates;
    }


    public int getHowLongWaitDriverAssign() {
        return howLongWaitDriverAssign;
    }

    public void setHowLongWaitDriverAssign(int howLongWaitDriverAssign) {
        this.howLongWaitDriverAssign = howLongWaitDriverAssign;
    }

    public SupportPhones getSupportPhones() {
        return supportPhones;
    }

    public void setSupportPhones(SupportPhones supportPhones) {
        this.supportPhones = supportPhones;
    }

    public ServerStateInfoV2 getServerStateInfo() {
        return serverStateInfo;
    }

    public void setServerStateInfo(ServerStateInfoV2 serverStateInfo) {
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
}
