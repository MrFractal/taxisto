package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 12.11.2014.
 */

public class CanceledMissionListRequest {
    private long timeOfCanceledStart;
    private long timeOfCanceledEnd;
    private long cancelById;
    private long taxoparkId;
    private String cancelBy; // (client, driver, operator, server)
    private int numberPage;
    private int sizePage;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
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

    public long getTaxoparkId() {
        return taxoparkId;
    }

    public void setTaxoparkId(long taxoparkId) {
        this.taxoparkId = taxoparkId;
    }

    public String getCancelBy() {
        return cancelBy;
    }

    public void setCancelBy(String cancelBy) {
        this.cancelBy = cancelBy;
    }

    public long getTimeOfCanceledStart() {
        return timeOfCanceledStart;
    }

    public void setTimeOfCanceledStart(long timeOfCanceledStart) {
        this.timeOfCanceledStart = timeOfCanceledStart;
    }

    public long getTimeOfCanceledEnd() {
        return timeOfCanceledEnd;
    }

    public void setTimeOfCanceledEnd(long timeOfCanceledEnd) {
        this.timeOfCanceledEnd = timeOfCanceledEnd;
    }

    public long getCancelById() {
        return cancelById;
    }

    public void setCancelById(long cancelById) {
        this.cancelById = cancelById;
    }
}
