package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.CancelMissionInfo;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 12.11.2014.
 */
public class CanceledMissionListResponse {
    private ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();
    private List<CancelMissionInfo> cancelMissionInfoList = new ArrayList<CancelMissionInfo>();
    private long lastPageNumber;
    private Long totalItems;
    private int countOperator;
    private int countDriver;
    private int countClient;
    private int countServer;

    public int getCountOperator() {
        return countOperator;
    }

    public void setCountOperator(int countOperator) {
        this.countOperator = countOperator;
    }

    public int getCountDriver() {
        return countDriver;
    }

    public void setCountDriver(int countDriver) {
        this.countDriver = countDriver;
    }

    public int getCountClient() {
        return countClient;
    }

    public void setCountClient(int countClient) {
        this.countClient = countClient;
    }

    public int getCountServer() {
        return countServer;
    }

    public void setCountServer(int countServer) {
        this.countServer = countServer;
    }

    public long getLastPageNumber() {
        return lastPageNumber;
    }

    public void setLastPageNumber(long lastPageNumber) {
        this.lastPageNumber = lastPageNumber;
    }

    public Long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Long totalItems) {
        this.totalItems = totalItems;
    }

    public ErrorCodeHelper getErrorCodeHelper() {
        return errorCodeHelper;
    }

    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
        this.errorCodeHelper = errorCodeHelper;
    }

    public List<CancelMissionInfo> getCancelMissionInfoList() {
        return cancelMissionInfoList;
    }

    public void setCancelMissionInfoList(List<CancelMissionInfo> cancelMissionInfoList) {
        this.cancelMissionInfoList = cancelMissionInfoList;
    }

    /*
дата/время заказа;
дата/время отмены;
статус перед отменой (если можно);
клиент (id);
телефон клиента;
водитель (id);
телефон водителя;
     */
}
