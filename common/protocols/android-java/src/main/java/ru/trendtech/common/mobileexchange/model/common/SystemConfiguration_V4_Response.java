package ru.trendtech.common.mobileexchange.model.common;

import ru.trendtech.common.mobileexchange.model.common.rates.*;
import ru.trendtech.common.mobileexchange.model.common.rates.MissionRateInfoV2;
import ru.trendtech.common.mobileexchange.model.common.states.ServerStateInfo;
import ru.trendtech.common.mobileexchange.model.common.support.SupportPhones;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 13.02.2015.
 */
public class SystemConfiguration_V4_Response {
    private List<ru.trendtech.common.mobileexchange.model.common.rates.MissionRateInfoV2> rates = new ArrayList<MissionRateInfoV2>();
    private List<MissionInfo> history = new ArrayList<MissionInfo>();
    private int bookedNew = 0;
    private int bookedToMe = 0;
    private int howLongWaitDriverAssign = 0;
    private SupportPhones supportPhones = new SupportPhones();
    private ServerStateInfo serverStateInfo = new ServerStateInfo();
    private String useMap;// 1 - 2gis, 2 - yandex
    private boolean isLate;
    private ItemLocation driverCurrentLocation;


    public ItemLocation getDriverCurrentLocation() {
        return driverCurrentLocation;
    }

    public void setDriverCurrentLocation(ItemLocation driverCurrentLocation) {
        this.driverCurrentLocation = driverCurrentLocation;
    }

    public List<MissionRateInfoV2> getRates() {
        return rates;
    }

    public void setRates(List<MissionRateInfoV2> rates) {
        this.rates = rates;
    }

    public List<MissionInfo> getHistory() {
        return history;
    }

    public void setHistory(List<MissionInfo> history) {
        this.history = history;
    }

    public int getBookedNew() {
        return bookedNew;
    }

    public void setBookedNew(int bookedNew) {
        this.bookedNew = bookedNew;
    }

    public int getBookedToMe() {
        return bookedToMe;
    }

    public void setBookedToMe(int bookedToMe) {
        this.bookedToMe = bookedToMe;
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
}
