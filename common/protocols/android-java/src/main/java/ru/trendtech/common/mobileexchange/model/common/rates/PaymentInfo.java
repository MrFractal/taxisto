package ru.trendtech.common.mobileexchange.model.common.rates;

import java.util.ArrayList;
import java.util.List;

public class PaymentInfo {
    private long rateId;
    private int missionTime;
    private long distanceExpected;
    private long distanceInFact;
    private int billingStatus = 0; // 0 - not paid, 1 - card, 2 - cash, 3 - bonus
    private String comment;
    private boolean fixedPrice;
    private List<ServicePriceInfo> services = new ArrayList<ServicePriceInfo>();
    private List<Integer> options = new ArrayList<Integer>();
    private boolean useBonuses;
    private double bonusesAmount;

    private int waitingOverFree;
    private double waitingPrice;
    private int pausesCount;
    private int pausesTime;
    private double pausesPrice;
    private double totalPrice;
    private double expectedPrice;
    private boolean rerouting;
    private int waitingTime;

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public boolean isRerouting() {
        return rerouting;
    }

    public void setRerouting(boolean rerouting) {
        this.rerouting = rerouting;
    }

    public double getWaitingPrice() {
        return waitingPrice;
    }

    public void setWaitingPrice(double waitingPrice) {
        this.waitingPrice = waitingPrice;
    }

    public double getPausesPrice() {
        return pausesPrice;
    }

    public void setPausesPrice(double pausesPrice) {
        this.pausesPrice = pausesPrice;
    }

    public double getExpectedPrice() {
        return expectedPrice;
    }

    public void setExpectedPrice(double expectedPrice) {
        this.expectedPrice = expectedPrice;
    }

    public double getPausesTotal() {return pausesPrice;}

    public void setPausesTotal(double pausesTotal) {this.pausesPrice = pausesTotal;}

    public List<Integer> getOptions() {
        return options;
    }

    public void setOptions(List<Integer> options) {
        this.options = options;
    }

    public long getRateId() {
        return rateId;
    }

    public void setRateId(long rateId) {
        this.rateId = rateId;
    }

    public boolean isUseBonuses() {
        return useBonuses;
    }

    public void setUseBonuses(boolean useBonuses) {
        this.useBonuses = useBonuses;
    }

    public long getDistanceInFact() {
        return distanceInFact;
    }

    public void setDistanceInFact(long distanceInFact) {
        this.distanceInFact = distanceInFact;
    }

    public double getBonusesAmount() {
        return bonusesAmount;
    }

    public void setBonusesAmount(double bonusesAmount) {
        this.bonusesAmount = bonusesAmount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getMissionTime() {
        return missionTime;
    }

    public void setMissionTime(int missionTime) {
        this.missionTime = missionTime;
    }

    public int getPausesCount() {
        return pausesCount;
    }

    public void setPausesCount(int pausesCount) {
        this.pausesCount = pausesCount;
    }

    public int getPausesTime() {
        return pausesTime;
    }

    public void setPausesTime(int pausesTime) {
        this.pausesTime = pausesTime;
    }

    public long getDistanceExpected() {
        return distanceExpected;
    }

    public void setDistanceExpected(long distanceExpected) {
        this.distanceExpected = distanceExpected;
    }

    public int getWaitingOverFree() {
        return waitingOverFree;
    }

    public void setWaitingOverFree(int waitingOverFree) {
        this.waitingOverFree = waitingOverFree;
    }

    public int getBillingStatus() {
        return billingStatus;
    }

    public void setBillingStatus(int billingStatus) {
        this.billingStatus = billingStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isFixedPrice() {
        return fixedPrice;
    }

    public void setFixedPrice(boolean fixedPrice) {
        this.fixedPrice = fixedPrice;
    }

    public List<ServicePriceInfo> getServices() {
        return services;
    }

    public void setServices(List<ServicePriceInfo> services) {
        this.services = services;
    }
}
