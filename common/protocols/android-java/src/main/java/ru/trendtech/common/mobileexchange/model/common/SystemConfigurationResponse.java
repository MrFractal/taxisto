package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.rates.MissionRateInfo;
import ru.trendtech.common.mobileexchange.model.common.states.ServerStateInfo;
import ru.trendtech.common.mobileexchange.model.common.support.SupportPhones;

import java.util.ArrayList;
import java.util.List;

/**
 * File created by max on 08/05/2014 13:35.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemConfigurationResponse {
    private List<MissionRateInfo> rates = new ArrayList<MissionRateInfo>();
    private List<MissionInfo> history = new ArrayList<MissionInfo>();

    private int bookedNew = 0;
    private int bookedToMe = 0;

    private int howLongWaitDriverAssign = 0;

    private SupportPhones supportPhones = new SupportPhones();

    private ServerStateInfo serverStateInfo = new ServerStateInfo();

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
