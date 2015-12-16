package ru.trendtech.domain;

import org.joda.time.DateTime;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created by petr on 10.02.2015.
 */
@Entity
@Table(name = "send_sms_lock")
public class SendSmsLock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "time_of_lock")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfLock;

    @Column(name = "time_of_unlock")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfUnlock;

    @Column(name = "auto_unlock_time")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime autoUnlockTime;

    @Column(name = "reason")
    private String reason;


    public DateTime getAutoUnlockTime() {
        return autoUnlockTime;
    }

    public void setAutoUnlockTime(DateTime autoUnlockTime) {
        this.autoUnlockTime = autoUnlockTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public DateTime getTimeOfUnlock() {
        return timeOfUnlock;
    }

    public void setTimeOfUnlock(DateTime timeOfUnlock) {
        this.timeOfUnlock = timeOfUnlock;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
