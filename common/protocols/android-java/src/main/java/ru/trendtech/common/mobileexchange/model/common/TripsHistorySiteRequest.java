package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 29.09.2014.
 */
public class TripsHistorySiteRequest {
    private long requesterId;
    private String security_token;
    private int numberPage;
    private int sizePage;
    private long dateStart;
    private long dateEnd;

    public long getDateStart() {
        return dateStart;
    }

    public void setDateStart(long dateStart) {
        this.dateStart = dateStart;
    }

    public long getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(long dateEnd) {
        this.dateEnd = dateEnd;
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

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public TripsHistorySiteRequest(long requesterId) {
        this.requesterId = requesterId;
    }

    public TripsHistorySiteRequest() {
        super();
    }

    public long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(long requesterId) {
        this.requesterId = requesterId;
    }
}
