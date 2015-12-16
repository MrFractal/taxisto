package ru.trendtech.common.mobileexchange.model.common.corporate;

/**
 * Created by petr on 19.03.2015.
 */
public class CorporateClientLocksInfo {
    private Long id;
    private long clientId;
    private long timeOfLock;
    private String reason;
    private long timeOfUnlock;
    private String lockedBy; /* ADMIN, MAIN_CLIENT*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getTimeOfLock() {
        return timeOfLock;
    }

    public void setTimeOfLock(long timeOfLock) {
        this.timeOfLock = timeOfLock;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getTimeOfUnlock() {
        return timeOfUnlock;
    }

    public void setTimeOfUnlock(long timeOfUnlock) {
        this.timeOfUnlock = timeOfUnlock;
    }

    public String getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }
}
