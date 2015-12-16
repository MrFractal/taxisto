package ru.trendtech.common.mobileexchange.model.common.php;

/**
 * File created by max on 08/05/2014 13:21.
 */


public class AutoClassRateInfoPHP {
    private int autoClass;
    private String perKmCurrency;

    private int perKmAmount;
    private int minAmount;
    private int kmIncluded;
    private int freeWaitMinutes;
    private int perMinuteWaitAmount;
    private int perHourAmount;

    public AutoClassRateInfoPHP() {
    }

    public AutoClassRateInfoPHP(int autoClass, int priceKm) {
        this.autoClass = autoClass;
        this.minAmount = priceKm;
    }

    public AutoClassRateInfoPHP(int autoClass, int perKmAmount, int minAmount, int kmIncluded, int freeWaitMinutes, int perMinuteWaitAmount, int perHourAmount) {
        this.autoClass = autoClass;
        this.perKmAmount = perKmAmount;
        this.minAmount = minAmount;
        this.kmIncluded = kmIncluded;
        this.freeWaitMinutes = freeWaitMinutes;
        this.perMinuteWaitAmount = perMinuteWaitAmount;
        this.perHourAmount = perHourAmount;
    }

    public AutoClassRateInfoPHP(int autoClass, String perKmCurrency, int perKmAmount, int minAmount, int kmIncluded, int freeWaitMinutes, int perMinuteWaitAmount, int perHourAmount) {
        this.autoClass = autoClass;
        this.perKmCurrency = perKmCurrency;
        this.perKmAmount = perKmAmount;
        this.minAmount = minAmount;
        this.kmIncluded = kmIncluded;
        this.freeWaitMinutes = freeWaitMinutes;
        this.perMinuteWaitAmount = perMinuteWaitAmount;
        this.perHourAmount = perHourAmount;
    }

    public int getAutoClass() {
        return autoClass;
    }

    public void setAutoClass(int autoClass) {
        this.autoClass = autoClass;
    }

    public double getPriceKm() {
        return minAmount;
    }

    public void setPriceKm(int priceKm) {
        this.minAmount = priceKm;
    }

    public void setPerKmCurrency(String perKmCurrency) {
        this.perKmCurrency = perKmCurrency;
    }

    public void setPerKmAmount(int perKmAmount) {
        this.perKmAmount = perKmAmount;
    }

    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
    }

    public void setKmIncluded(int kmIncluded) {
        this.kmIncluded = kmIncluded;
    }

    public void setFreeWaitMinutes(int freeWaitMinutes) {
        this.freeWaitMinutes = freeWaitMinutes;
    }

    public void setPerMinuteWaitAmount(int perMinuteWaitAmount) {
        this.perMinuteWaitAmount = perMinuteWaitAmount;
    }

    public void setPerHourAmount(int perHourAmount) {
        this.perHourAmount = perHourAmount;
    }

    public String getPerKmCurrency() {

        return perKmCurrency;
    }

    public int getPerKmAmount() {
        return perKmAmount;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getKmIncluded() {
        return kmIncluded;
    }

    public int getFreeWaitMinutes() {
        return freeWaitMinutes;
    }

    public int getPerMinuteWaitAmount() {
        return perMinuteWaitAmount;
    }

    public int getPerHourAmount() {
        return perHourAmount;
    }
}
