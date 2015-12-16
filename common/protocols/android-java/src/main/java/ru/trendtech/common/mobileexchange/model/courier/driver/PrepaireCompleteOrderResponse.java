package ru.trendtech.common.mobileexchange.model.courier.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 12.10.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrepaireCompleteOrderResponse extends ErrorCodeHelper {
    private int priceItems;
    private int sumToCourier;
    private int generalPrice;

    public int getPriceItems() {
        return priceItems;
    }

    public void setPriceItems(int priceItems) {
        this.priceItems = priceItems;
    }

    public int getSumToCourier() {
        return sumToCourier;
    }

    public void setSumToCourier(int sumToCourier) {
        this.sumToCourier = sumToCourier;
    }

    public int getGeneralPrice() {
        return generalPrice;
    }

    public void setGeneralPrice(int generalPrice) {
        this.generalPrice = generalPrice;
    }
}
