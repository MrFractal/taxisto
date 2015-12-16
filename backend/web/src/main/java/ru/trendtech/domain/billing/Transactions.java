package ru.trendtech.domain.billing;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import org.joda.time.DateTime;
import ru.trendtech.domain.Mission;

import javax.persistence.*;

@Entity
@Table(name = "transactions")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "time_created")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;

    @ManyToOne
    @JoinColumn(name = "from_id", nullable = false, updatable = false)
    private Account from;

    @ManyToOne
    @JoinColumn(name = "to_id", nullable = false, updatable = false)
    private Account to;

    @Columns(columns = {@Column(name = "money_currency"), @Column(name = "money_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money money;

    @Column(name = "time_opened")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOpened;

    @Column(name = "time_declined")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeDeclined;

    @Column(name = "time_billed")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeBilled;

    @Column(name = "time_archived")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeArchived;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "mission_id", nullable = false, updatable = false)
    private Mission mission;

    @Column(name = "payment_type")
    @Enumerated(value = EnumType.STRING)
    private PaymentType paymentType;

    @Column(name = "state")
    @Enumerated(value = EnumType.STRING)
    private State state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Account getFrom() {
        return from;
    }

    public void setFrom(Account from) {
        this.from = from;
    }

    public Account getTo() {
        return to;
    }

    public void setTo(Account to) {
        this.to = to;
    }

    public Money getMoney() {
        return money;
    }

    public void setMoney(Money money) {
        this.money = money;
    }

    public DateTime getTimeOpened() {
        return timeOpened;
    }

    public void setTimeOpened(DateTime timeOpened) {
        this.timeOpened = timeOpened;
    }

    public DateTime getTimeDeclined() {
        return timeDeclined;
    }

    public void setTimeDeclined(DateTime timeDeclined) {
        this.timeDeclined = timeDeclined;
    }

    public DateTime getTimeBilled() {
        return timeBilled;
    }

    public void setTimeBilled(DateTime timeBilled) {
        this.timeBilled = timeBilled;
    }

    public DateTime getTimeArchived() {
        return timeArchived;
    }

    public void setTimeArchived(DateTime timeArchived) {
        this.timeArchived = timeArchived;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "";
        /*
                "Transactions{" +
                "id=" + id +
                ", createdDateTime=" + createdDateTime +
                ", from=" + from +
                ", to=" + to +
                ", money=" + money +
                ", timeOpened=" + timeOpened +
                ", timeDeclined=" + timeDeclined +
                ", timeBilled=" + timeBilled +
                ", timeArchived=" + timeArchived +
                ", comment='" + comment + '\'' +
                ", mission=" + mission +
                ", paymentType=" + paymentType +
                ", state=" + state +
                '}';
        */
    }

    public static enum State {
        OPEN,
        DECLINED,
        BILLED,
        ARCHIVED,;
    }
}
