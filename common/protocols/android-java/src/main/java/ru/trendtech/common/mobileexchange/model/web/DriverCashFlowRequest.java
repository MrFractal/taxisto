package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 10.02.2015.
 */
/*
Фильтры: дата/время, таксопарк, водитель, клиент.
 */
public class DriverCashFlowRequest {
    private int numberPage;
    private int sizePage;
    private long taxoparkId;
    private long driverId;
    private long clientId;
    private long startTime;
    private long endTime;
    private String security_token;

    public int getNumberPage() {
        return numberPage;
    }

    public void setNumberPage(int numberPage) {
        this.numberPage = numberPage;
    }

    public int getSizePage() {
        return sizePage;
    }

    public void setSizePage(int sizePage) {
        this.sizePage = sizePage;
    }

    public long getTaxoparkId() {
        return taxoparkId;
    }

    public void setTaxoparkId(long taxoparkId) {
        this.taxoparkId = taxoparkId;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }
}
