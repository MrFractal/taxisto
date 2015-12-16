package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 13.10.2014.
 */

public class ClientSumPromoCodeInsertRequest {
    private long clientId;
    private int sum;

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
