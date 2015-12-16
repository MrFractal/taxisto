package ru.trendtech.domain;

import javax.persistence.*;

/**
 * Created by petr on 08.10.2014.
 */

@Entity
@Table(name = "client_locks")
public class ClientLocks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "client_id")
    private long clientId;

    @Column(name = "time_of_lock")
    private Long timeOfLock;

    @Column(name = "reason")
    private String reason;

    @Column(name = "time_of_unlock")
    private Long timeOfUnlock;


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

    @Override
    public String toString() {
        return "";
    }

}

