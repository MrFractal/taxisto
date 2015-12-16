package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 11.10.2014.
 */
public class ClientLocksInfo {
    private long id;
    private long clientId;
    private Long timeOfLock;
    private String reason;
    private Long timeOfUnlock;
    private String administrativeStatus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public Long getTimeOfLock() {
        return timeOfLock;
    }

    public void setTimeOfLock(Long timeOfLock) {
        this.timeOfLock = timeOfLock;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getTimeOfUnlock() {
        return timeOfUnlock;
    }

    public void setTimeOfUnlock(Long timeOfUnlock) {
        this.timeOfUnlock = timeOfUnlock;
    }

    public String getAdministrativeStatus() {
        return administrativeStatus;
    }

    public void setAdministrativeStatus(String administrativeStatus) {
        this.administrativeStatus = administrativeStatus;
    }
}
