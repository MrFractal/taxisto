package ru.trendtech.common.mobileexchange.model.common.rates;

import java.util.ArrayList;
import java.util.List;

public class MissionRateInfo {
    private long id;
    private String name;
    private double priceMinute;
    private double priceMinimal;
    private double priceStop;
    private int freeWaitingTime;
    private List<AutoClassRateInfo> autoClassRateInfos = new ArrayList<AutoClassRateInfo>();
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

    public List<AutoClassRateInfo> getAutoClassRateInfos() {
        return autoClassRateInfos;
    }

    public void setAutoClassRateInfos(List<AutoClassRateInfo> autoClassRateInfos) {
        this.autoClassRateInfos = autoClassRateInfos;
    }

    public List<ServicePriceInfo> getServicesPrices() {
        return servicesPrices;
    }

    public void setServicesPrices(List<ServicePriceInfo> servicesPrices) {
        this.servicesPrices = servicesPrices;
    }
}
