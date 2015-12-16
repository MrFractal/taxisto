package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 31.08.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourierCalculatePriceResponse extends ErrorCodeHelper {
    private int priceItems; // стоимость за товар
    private int priceOfInsurance; // оценочная стоимость (забрать и привезти)
    private int priceByPercentInsurance; // сумма процента от оценочной стоимости
    private int priceDelivery; // стоимость доставки
    private int priceByAdditionalAddress;
    private int commonPrice; // общая стоимость = стоимость за товар + стоимость доставки + (% увеличения за стоимость или % от страховки)
    private int distance;

    public int getPriceByAdditionalAddress() {
        return priceByAdditionalAddress;
    }

    public void setPriceByAdditionalAddress(int priceByAdditionalAddress) {
        this.priceByAdditionalAddress = priceByAdditionalAddress;
    }

    public int getPriceByPercentInsurance() {
        return priceByPercentInsurance;
    }

    public void setPriceByPercentInsurance(int priceByPercentInsurance) {
        this.priceByPercentInsurance = priceByPercentInsurance;
    }

    public int getPriceOfInsurance() {
        return priceOfInsurance;
    }

    public void setPriceOfInsurance(int priceOfInsurance) {
        this.priceOfInsurance = priceOfInsurance;
    }

    public int getCommonPrice() {
        return commonPrice;
    }

    public void setCommonPrice(int commonPrice) {
        this.commonPrice = commonPrice;
    }

    public int getPriceDelivery() {
        return priceDelivery;
    }

    public void setPriceDelivery(int priceDelivery) {
        this.priceDelivery = priceDelivery;
    }

    public int getPriceItems() {
        return priceItems;
    }

    public void setPriceItems(int priceItems) {
        this.priceItems = priceItems;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
