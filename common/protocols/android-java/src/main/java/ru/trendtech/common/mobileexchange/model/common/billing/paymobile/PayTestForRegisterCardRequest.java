package ru.trendtech.common.mobileexchange.model.common.billing.paymobile;

/**
 * Created by petr on 17.10.2014.
 */
public class PayTestForRegisterCardRequest {
    private String orderNumber;
    private long clientId;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }
}
