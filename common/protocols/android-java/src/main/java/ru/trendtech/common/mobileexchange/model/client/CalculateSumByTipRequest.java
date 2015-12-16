package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 06.02.2015.
 */
public class CalculateSumByTipRequest {
    private String security_token;
    private long clientId;
    private long tipPercentId;
    private int sum;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getTipPercentId() {
        return tipPercentId;
    }

    public void setTipPercentId(long tipPercentId) {
        this.tipPercentId = tipPercentId;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
