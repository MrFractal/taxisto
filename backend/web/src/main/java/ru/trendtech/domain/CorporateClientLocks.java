package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 19.03.2015.
 */
@Entity
@Table(name = "corporate_client_locks")
public class CorporateClientLocks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "time_of_lock")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfLock;

    @Column(name = "reason")
    private String reason;

    @Column(name = "time_of_unlock")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfUnlock;

    @Column(name = "locked_by")
    @Enumerated(value = EnumType.STRING)
    private LockedBy lockedBy;

    public enum LockedBy{
        ADMIN,
        MAIN_CLIENT
    }

    public LockedBy getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(LockedBy lockedBy) {
        this.lockedBy = lockedBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public DateTime getTimeOfLock() {
        return timeOfLock;
    }

    public void setTimeOfLock(DateTime timeOfLock) {
        this.timeOfLock = timeOfLock;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public DateTime getTimeOfUnlock() {
        return timeOfUnlock;
    }

    public void setTimeOfUnlock(DateTime timeOfUnlock) {
        this.timeOfUnlock = timeOfUnlock;
    }
}
