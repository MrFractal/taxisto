package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 07.10.2014.
 */
public class MoneyWithdrawalRequest {
    private long driverId;
    private String smsCode;
    private int countSymbols=6;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public int getCountSymbols() {
        return countSymbols;
    }

    public void setCountSymbols(int countSymbols) {
        this.countSymbols = countSymbols;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
