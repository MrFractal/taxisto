package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 07.10.2014.
 */
public class MoneyWithdrawalInfo {
    private Long id;
    private String smsCode;
    private long driverId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }
}
