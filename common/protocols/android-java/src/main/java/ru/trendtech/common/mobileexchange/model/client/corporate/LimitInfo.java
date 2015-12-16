package ru.trendtech.common.mobileexchange.model.client.corporate;

/**
 * Created by petr on 19.03.2015.
 */
public class LimitInfo {
    private long limitId;
    private long clientId;
    private long mainClientId;
    private int limitAmount;
    private int typePeriod;

    public long getMainClientId() {
        return mainClientId;
    }

    public void setMainClientId(long mainClientId) {
        this.mainClientId = mainClientId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getLimitId() {
        return limitId;
    }

    public void setLimitId(long limitId) {
        this.limitId = limitId;
    }

    public int getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(int limitAmount) {
        this.limitAmount = limitAmount;
    }

    public int getTypePeriod() {
        return typePeriod;
    }

    public void setTypePeriod(int typePeriod) {
        this.typePeriod = typePeriod;
    }
}
