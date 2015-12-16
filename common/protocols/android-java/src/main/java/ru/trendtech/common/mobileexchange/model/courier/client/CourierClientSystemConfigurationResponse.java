package ru.trendtech.common.mobileexchange.model.courier.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.courier.OrderInfo;
import ru.trendtech.common.mobileexchange.model.courier.CustomWindowInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 03.09.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourierClientSystemConfigurationResponse extends ErrorCodeHelper {
    private CustomWindowInfo customWindow;
    private int bookedRangeMin;
    private int bookedRangeMax;
    private int percentInsurance;
    private OrderInfo orderInfo;

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public CustomWindowInfo getCustomWindow() {
        return customWindow;
    }

    public void setCustomWindow(CustomWindowInfo customWindow) {
        this.customWindow = customWindow;
    }

    public int getPercentInsurance() {
        return percentInsurance;
    }

    public void setPercentInsurance(int percentInsurance) {
        this.percentInsurance = percentInsurance;
    }

    public int getBookedRangeMin() {
        return bookedRangeMin;
    }

    public void setBookedRangeMin(int bookedRangeMin) {
        this.bookedRangeMin = bookedRangeMin;
    }

    public int getBookedRangeMax() {
        return bookedRangeMax;
    }

    public void setBookedRangeMax(int bookedRangeMax) {
        this.bookedRangeMax = bookedRangeMax;
    }

}
