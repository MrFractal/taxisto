package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 13.10.2014.
 */
public class ClientSumPromoCodeUpdateRequest {
    private long clientSumPromoId;
    private int sum;

    public long getClientSumPromoId() {
        return clientSumPromoId;
    }

    public void setClientSumPromoId(long clientSumPromoId) {
        this.clientSumPromoId = clientSumPromoId;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
