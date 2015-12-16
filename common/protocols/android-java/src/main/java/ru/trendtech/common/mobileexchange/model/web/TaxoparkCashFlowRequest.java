package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 20.07.2015.
 */
public class TaxoparkCashFlowRequest {
    private String security_token;
    private long startTime;
    private long endTime;
    private long taxoparkId;
    private int numberPage;
    private int sizePage;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
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

    public long getTaxoparkId() {
        return taxoparkId;
    }

    public void setTaxoparkId(long taxoparkId) {
        this.taxoparkId = taxoparkId;
    }

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
}
