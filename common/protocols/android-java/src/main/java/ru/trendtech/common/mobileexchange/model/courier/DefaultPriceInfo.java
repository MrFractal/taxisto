package ru.trendtech.common.mobileexchange.model.courier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 21.08.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultPriceInfo {
    private Long id;
    private int minimalPrice;
    private int perKmPrice;
    private int kmIncluded;
    private int orderProcessingPrice;
    private double percentOrderProcessing;
    private int orderType; // [UNKNOWN(0),BUY_AND_DELIVER(1),TAKE_AND_DELIVER(2),OTHER(3)]
    private boolean active;
    private int fineAmount;

    public int getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(int fineAmount) {
        this.fineAmount = fineAmount;
    }

    public double getPercentOrderProcessing() {
        return percentOrderProcessing;
    }

    public void setPercentOrderProcessing(double percentOrderProcessing) {
        this.percentOrderProcessing = percentOrderProcessing;
    }

    public int getKmIncluded() {
        return kmIncluded;
    }

    public void setKmIncluded(int kmIncluded) {
        this.kmIncluded = kmIncluded;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMinimalPrice() {
        return minimalPrice;
    }

    public void setMinimalPrice(int minimalPrice) {
        this.minimalPrice = minimalPrice;
    }

    public int getPerKmPrice() {
        return perKmPrice;
    }

    public void setPerKmPrice(int perKmPrice) {
        this.perKmPrice = perKmPrice;
    }

    public int getOrderProcessingPrice() {
        return orderProcessingPrice;
    }

    public void setOrderProcessingPrice(int orderProcessingPrice) {
        this.orderProcessingPrice = orderProcessingPrice;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
