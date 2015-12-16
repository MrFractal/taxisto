package ru.trendtech.common.mobileexchange.model.common.rates;

/**
 * File created by max on 08/05/2014 13:21.
 */
public class AutoClassRateInfo {
    private int autoClass;
    private int price;
    private int kmIncluded;
    private int priceKm;
    private int priceHour;
    private int freeWaitMinutes;
    private int perMinuteWaitAmount;
    private int intercity;
    //private int perHourAmount;


    public AutoClassRateInfo() {
    }


    public AutoClassRateInfo(int autoClass, int price, int kmIncluded, int priceKm, int priceHour,
                             int freeWaitMinutes, int perMinuteWaitAmount, int intercity) {
        this.autoClass = autoClass;
        this.price = price;
        this.priceKm = priceKm;
        this.kmIncluded = kmIncluded;
        this.priceKm = priceKm;
        this.priceHour = priceHour;
        this.freeWaitMinutes = freeWaitMinutes;
        this.perMinuteWaitAmount = perMinuteWaitAmount;
        this.intercity = intercity;
        //this.perHourAmount = perHourAmount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAutoClass() {
        return autoClass;
    }

    public void setAutoClass(int autoClass) {
        this.autoClass = autoClass;
    }

    public int getKmIncluded() {
        return kmIncluded;
    }

    public void setKmIncluded(int kmIncluded) {
        this.kmIncluded = kmIncluded;
    }

    public int getPriceKm() {
        return priceKm;
    }

    public void setPriceKm(int priceKm) {
        this.priceKm = priceKm;
    }

    public int getPriceHour() {
        return priceHour;
    }

    public void setPriceHour(int priceHour) {
        this.priceHour = priceHour;
    }

    public int getFreeWaitMinutes() {
        return freeWaitMinutes;
    }

    public void setFreeWaitMinutes(int freeWaitMinutes) {
        this.freeWaitMinutes = freeWaitMinutes;
    }

    public int getPerMinuteWaitAmount() {
        return perMinuteWaitAmount;
    }

    public void setPerMinuteWaitAmount(int perMinuteWaitAmount) {
        this.perMinuteWaitAmount = perMinuteWaitAmount;
    }

    public int getIntercity() {
        return intercity;
    }

    public void setIntercity(int intercity) {
        this.intercity = intercity;
    }

}
