package ru.trendtech.common.mobileexchange.model.common.billing.paymobile;

/**
 * Created by petr on 15.09.2014.
 */

public class GetIdCardRequest {
    private long clientId;
    private String mdOrder;

    public String getMdOrder() {
        return mdOrder;
    }

    public void setMdOrder(String mdOrder) {
        this.mdOrder = mdOrder;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }
}
