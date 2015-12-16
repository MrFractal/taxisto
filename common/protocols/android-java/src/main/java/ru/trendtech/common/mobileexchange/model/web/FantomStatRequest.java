package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 09.06.2015.
 */
public class FantomStatRequest {
    /*
1. Дата с по
2. Клиент
3. Фантом
4. Отменен/выполнен/назначен/едем
5. Зона
     */
    private String security_token;
    private long startTime;
    private long endTime;
    private long clientId;
    private long fantomDriverId;
    private String state; // CANCELED, COMPLETED, ASSIGNED, IN_TRIP
    private long regionId;
    private int numPage;
    private int pageSize;

    public int getNumPage() {
        return numPage;
    }

    public void setNumPage(int numPage) {
        this.numPage = numPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

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

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getFantomDriverId() {
        return fantomDriverId;
    }

    public void setFantomDriverId(long fantomDriverId) {
        this.fantomDriverId = fantomDriverId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getRegionId() {
        return regionId;
    }

    public void setRegionId(long regionId) {
        this.regionId = regionId;
    }
}
