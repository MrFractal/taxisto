package ru.trendtech.common.mobileexchange.model.courier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 21.08.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItemPriceInfo {
    private long id;
    private ItemPriceInfo itemPriceInfo;
    private int priceOnDayOfOrder; // стоимость в день оформления заказа за еденицу товара (пусто если услуга)
//    private int percentInsuranceOnDayOfOrder; // % страховки в день покупки
//    private int priceOfInsurance; // оценочная стоимость(страховка товара)
    private int countItem;
    private OrderInfo orderInfo;

//    public int getPercentInsuranceOnDayOfOrder() {
//        return percentInsuranceOnDayOfOrder;
//    }
//
//    public void setPercentInsuranceOnDayOfOrder(int percentInsuranceOnDayOfOrder) {
//        this.percentInsuranceOnDayOfOrder = percentInsuranceOnDayOfOrder;
//    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ItemPriceInfo getItemPriceInfo() {
        return itemPriceInfo;
    }

    public void setItemPriceInfo(ItemPriceInfo itemPriceInfo) {
        this.itemPriceInfo = itemPriceInfo;
    }

    public int getPriceOnDayOfOrder() {
        return priceOnDayOfOrder;
    }

    public void setPriceOnDayOfOrder(int priceOnDayOfOrder) {
        this.priceOnDayOfOrder = priceOnDayOfOrder;
    }

//    public int getPriceOfInsurance() {
//        return priceOfInsurance;
//    }
//
//    public void setPriceOfInsurance(int priceOfInsurance) {
//        this.priceOfInsurance = priceOfInsurance;
//    }

    public int getCountItem() {
        return countItem;
    }

    public void setCountItem(int countItem) {
        this.countItem = countItem;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
}
