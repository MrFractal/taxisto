package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 12.11.2014.
 */
public class CancelMissionInfo {
    private long timeOfRequesting;
    private long timeOfCanceled;
    private ClientInfo clientInfo;
    private String phoneClient;
    private DriverInfoARM driverInfoARM;
    private String phoneDriver;
    private String cancelBy;
    private long missionId;
    private String stateBeforeCanceled;

    public String getStateBeforeCanceled() {
        return stateBeforeCanceled;
    }

    public void setStateBeforeCanceled(String stateBeforeCanceled) {
        this.stateBeforeCanceled = stateBeforeCanceled;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public String getCancelBy() {
        return cancelBy;
    }

    public void setCancelBy(String cancelBy) {
        this.cancelBy = cancelBy;
    }

    public long getTimeOfRequesting() {
        return timeOfRequesting;
    }

    public void setTimeOfRequesting(long timeOfRequesting) {
        this.timeOfRequesting = timeOfRequesting;
    }

    public long getTimeOfCanceled() {
        return timeOfCanceled;
    }

    public void setTimeOfCanceled(long timeOfCanceled) {
        this.timeOfCanceled = timeOfCanceled;
    }

    public String getPhoneClient() {
        return phoneClient;
    }

    public void setPhoneClient(String phoneClient) {
        this.phoneClient = phoneClient;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public DriverInfoARM getDriverInfoARM() {
        return driverInfoARM;
    }

    public void setDriverInfoARM(DriverInfoARM driverInfoARM) {
        this.driverInfoARM = driverInfoARM;
    }

    public String getPhoneDriver() {
        return phoneDriver;
    }

    public void setPhoneDriver(String phoneDriver) {
        this.phoneDriver = phoneDriver;
    }
}
