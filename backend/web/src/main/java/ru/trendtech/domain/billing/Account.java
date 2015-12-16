package ru.trendtech.domain.billing;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import org.joda.time.DateTime;
import ru.trendtech.utils.MoneyUtils;

import javax.persistence.*;

/**
 * File created by ivanenok on 15/04/2014 23:35.
 */
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "time_created")
//    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
//    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime",
//          parameters = {
//                    @org.hibernate.annotations.Parameter(name = "databaseZone", value = "UTC"),
//                    @org.hibernate.annotations.Parameter(name = "javaZone", value = "jvm")})
    private DateTime timeCreated;

    @Column(name = "time_activated")
//    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
//    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime",
//            parameters = {
//                    @org.hibernate.annotations.Parameter(name = "databaseZone", value = "UTC"),
//                    @org.hibernate.annotations.Parameter(name = "javaZone", value = "Europe/Moscow")})
    private DateTime timeActivated;

    @Column(name = "time_paused")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
//    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime",
//            parameters = {
//                    @org.hibernate.annotations.Parameter(name = "databaseZone", value = "UTC"),
//                    @org.hibernate.annotations.Parameter(name = "javaZone", value = "jvm")})
    private DateTime timePaused;

    @Column(name = "time_blocked")
//    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeBlocked;

    @Column(name = "time_closed")
//    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeClosed;

    @Columns(columns = {@Column(name = "money_currency"), @Column(name = "money_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money money = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    @Columns(columns = {@Column(name = "bonuses_currency"), @Column(name = "bonuses_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money bonuses = Money.zero(MoneyUtils.BONUSES_CURRENCY);

    @Columns(columns = {@Column(name = "corporate_balance_currency"), @Column(name = "corporate_balance_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money corporateBalance = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    @Columns(columns = {@Column(name = "courier_bonuses_currency"), @Column(name = "courier_bonuses_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money courierBonuses = Money.zero(MoneyUtils.BONUSES_CURRENCY);

    @Columns(columns = {@Column(name = "credit_limit_currency"), @Column(name = "credit_limit_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money creditLimitMoney = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    @Column(name = "account_state")
    @Enumerated(value = EnumType.STRING)
    private State state;

    @Column(name = "account_type")
    @Enumerated(value = EnumType.STRING)
    private Kind kind;


    public Money getCourierBonuses() {
        return courierBonuses;
    }

    public void setCourierBonuses(Money courierBonuses) {
        this.courierBonuses = courierBonuses;
    }

    public Money getCorporateBalance() {
        return corporateBalance;
    }

    public void setCorporateBalance(Money corporateBalance) {
        this.corporateBalance = corporateBalance;
    }

    public Money getCreditLimitMoney() {
        return creditLimitMoney;
    }

    public void setCreditLimitMoney(Money creditLimitMoney) {
        this.creditLimitMoney = creditLimitMoney;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(DateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    public DateTime getTimeActivated() {
        return timeActivated;
    }

    public void setTimeActivated(DateTime timeActivated) {
        this.timeActivated = timeActivated;
    }

    public DateTime getTimePaused() {
        return timePaused;
    }

    public void setTimePaused(DateTime timePaused) {
        this.timePaused = timePaused;
    }

    public DateTime getTimeBlocked() {
        return timeBlocked;
    }

    public void setTimeBlocked(DateTime timeBlocked) {
        this.timeBlocked = timeBlocked;
    }

    public DateTime getTimeClosed() {
        return timeClosed;
    }

    public void setTimeClosed(DateTime timeClosed) {
        this.timeClosed = timeClosed;
    }

    public Money getMoney() {
        return money;
    }

    public void setMoney(Money money) {
        this.money = money;
    }

    public Money getBonuses() {
        return bonuses;
    }

    public void setBonuses(Money bonuses) {
        this.bonuses = bonuses;
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
                "Account{" +
                "state=" + state +
                ", bonuses=" + bonuses +
                ", money=" + money +
                ", timeClosed=" + timeClosed +
                ", timeBlocked=" + timeBlocked +
                ", timePaused=" + timePaused +
                ", timeActivated=" + timeActivated +
                ", timeCreated=" + timeCreated +
                ", id=" + id +
                '}';
        */
    }

    public static enum State {
        BONUSES_ONLY,
        ACTIVE,
        PAUSED,
        CLOSED,
        BLOCKED,;
    }

    public static enum Kind {
        INTERNAL,
        CLIENT,
        DRIVER,
        CORPORATE,
        ;
    }

}
