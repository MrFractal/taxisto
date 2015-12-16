package ru.trendtech.common.mobileexchange.model.common.rates;

/**
 * Created by petr on 17.12.2014.
 */
public class AutoClassRateInfoV2 {
    private int autoClass;
    private String autoClassStr;
    private int price;
    private int kmIncluded;
    private int priceKm;
    private int priceHour;
    private int freeWaitMinutes;
    private int perMinuteWaitAmount;
    private int intercity;
    private int perHourAmount;
    private String activePicUrl;
    private String notActivePicUrl;
    private String description;
    private String autoExample;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getActivePicUrl() {
        return activePicUrl;
    }

    public void setActivePicUrl(String activePicUrl) {
        this.activePicUrl = activePicUrl;
    }

    public String getNotActivePicUrl() {
        return notActivePicUrl;
    }

    public void setNotActivePicUrl(String notActivePicUrl) {
        this.notActivePicUrl = notActivePicUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAutoExample() {
        return autoExample;
    }

    public void setAutoExample(String autoExample) {
        this.autoExample = autoExample;
    }

    public int getAutoClass() {
        return autoClass;
    }

    public void setAutoClass(int autoClass) {
        this.autoClass = autoClass;
    }

    public String getAutoClassStr() {
        return autoClassStr;
    }

    public void setAutoClassStr(String autoClassStr) {
        this.autoClassStr = autoClassStr;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public int getPerHourAmount() {
        return perHourAmount;
    }

    public void setPerHourAmount(int perHourAmount) {
        this.perHourAmount = perHourAmount;
    }
}
