package ru.trendtech.common.mobileexchange.model.web;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 17.11.2014.
 */
public class AssistantStatListRequest {
    private long timeRequestingStart;
    private long timeRequestingEnd;
    private List<Long> assistantId = new ArrayList<Long>();
    private long taxoparkId;
    private long driverId;
    private int numberPage;
    private int sizePage;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getTimeRequestingStart() {
        return timeRequestingStart;
    }

    public void setTimeRequestingStart(long timeRequestingStart) {
        this.timeRequestingStart = timeRequestingStart;
    }

    public long getTimeRequestingEnd() {
        return timeRequestingEnd;
    }

    public void setTimeRequestingEnd(long timeRequestingEnd) {
        this.timeRequestingEnd = timeRequestingEnd;
    }

    public List<Long> getAssistantId() {
        return assistantId;
    }

    public void setAssistantId(List<Long> assistantId) {
        this.assistantId = assistantId;
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
