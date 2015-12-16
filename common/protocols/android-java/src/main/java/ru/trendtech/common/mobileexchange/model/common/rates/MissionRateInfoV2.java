package ru.trendtech.common.mobileexchange.model.common.rates;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 30.12.2014.
 */
public class MissionRateInfoV2 {
    private long id;
    private String name;
    private double priceMinute;
    private double priceMinimal;
    private double priceStop;
    private int freeWaitingTime;
    private List<AutoClassRateInfoV2> autoClassRateInfos = new ArrayList<AutoClassRateInfoV2>();
    private List<ServicePriceInfo> servicesPrices = new ArrayList<ServicePriceInfo>();


    public int getFreeWaitingTime() {
        return freeWaitingTime;
    }

    public void setFreeWaitingTime(int freeWaitingTime) {
        this.freeWaitingTime = freeWaitingTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPriceMinute() {
        return priceMinute;
    }

    public void setPriceMinute(double priceMinute) {
        this.priceMinute = priceMinute;
    }

    public double getPriceMinimal() {
        return priceMinimal;
    }

    public void setPriceMinimal(double priceMinimal) {
        this.priceMinimal = priceMinimal;
    }

    public double getPriceStop() {
        return priceStop;
    }

    public void setPriceStop(double priceStop) {
        this.priceStop = priceStop;
    }

    public List<AutoClassRateInfoV2> getAutoClassRateInfos() {
        return autoClassRateInfos;
    }

    public void setAutoClassRateInfos(List<AutoClassRateInfoV2> autoClassRateInfos) {
        this.autoClassRateInfos = autoClassRateInfos;
    }

    public List<ServicePriceInfo> getServicesPrices() {
        return servicesPrices;
    }

    public void setServicesPrices(List<ServicePriceInfo> servicesPrices) {
        this.servicesPrices = servicesPrices;
    }
}
