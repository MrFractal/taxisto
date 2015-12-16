package ru.trendtech.common.mobileexchange.model.common;

import ru.trendtech.common.mobileexchange.model.common.rates.MissionRateInfo;
import ru.trendtech.common.mobileexchange.model.common.states.ServerStateInfo;
import ru.trendtech.common.mobileexchange.model.common.support.SupportPhones;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 10.11.2014.
 */
public class SystemConfiguration_V2_Response {
    private List<MissionRateInfo> rates = new ArrayList<MissionRateInfo>();
    private List<MissionInfo> history = new ArrayList<MissionInfo>();

    private int bookedNew = 0;
    private int bookedToMe = 0;

    private int howLongWaitDriverAssign = 0;

    private SupportPhones supportPhones = new SupportPhones();

    private ServerStateInfo serverStateInfo = new ServerStateInfo();

    private String useMap;// 1 - 2gis, 2 - yandex

    public String getUseMap() {
        return useMap;
    }

    public void setUseMap(String useMap) {
        this.useMap = useMap;
    }

    public List<MissionRateInfo> getRates() {
        return rates;
    }

    public void setRates(List<MissionRateInfo> rates) {
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

    public ServerStateInfo getServerStateInfo() {
        return serverStateInfo;
    }

    public void setServerStateInfo(ServerStateInfo serverStateInfo) {
        this.serverStateInfo = serverStateInfo;
    }
}
