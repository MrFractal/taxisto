package ru.trendtech.domain.courier;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import org.joda.time.DateTime;
import ru.trendtech.domain.Client;
import ru.trendtech.utils.MoneyUtils;

import javax.persistence.*;
import java.sql.Clob;

/**
 * Created by petr on 14.09.2015.
 */
@Entity
@Table(name = "c_order_payment")
public class OrderPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToOne
    @JoinColumn(name = "c_client_card_id", nullable = false)
    private CourierClientCard courierClientCard;

    @OneToOne
    @JoinColumn(name = "c_order_id", nullable = false)
    private Order order;

    @Columns(columns = {@Column(name = "price_currency"), @Column(name = "price_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money priceAmount = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    @Columns(columns = {@Column(name = "price_in_fact_currency"), @Column(name = "price_amount_in_fact")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money priceAmountInFact = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    @Column(name = "time_requesting")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfRequesting;

    @Column(name = "payment_state_card", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PaymentState paymentState = PaymentState.NONE;

    @Column(name = "transaction_id") // , unique = true, length = 75 / , unique = true, length = 75, nullable = true
    private String transactionId;

    @Column(name = "acsurl")
    private String acsurl = "";

    @Column(name = "pareq")
    private String pareq = "";

    @Column(name = "pd")
    private String pd = "";

    @Column(name = "md")
    private String md = "";

    @Column(name = "redirect_url")
    private String redirectUrl = "";


    @Column(name = "message")
    private String message = "";

    @Column(name = "error_code")
    private String errorCode = "";

    @Column(name = "time_last_update")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfLastUpdate;

    @Column(name = "generate_number")
    private String generateNumber; // id + ":" + id_order


    public Money getPriceAmountInFact() {
        return priceAmountInFact;
    }

    public void setPriceAmountInFact(Money priceAmountInFact) {
        this.priceAmountInFact = priceAmountInFact;
    }

    public String getPd() {
        return pd;
    }

    public void setPd(String pd) {
        this.pd = pd;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getAcsurl() {
        return acsurl;
    }

    public void setAcsurl(String acsurl) {
        this.acsurl = acsurl;
    }

    public String getPareq() {
        return pareq;
    }

    public void setPareq(String pareq) {
        this.pareq = pareq;
    }

    public String getMd() {
        return md;
    }

    public void setMd(String md) {
        this.md = md;
    }

    public CourierClientCard getCourierClientCard() {
        return courierClientCard;
    }

    public void setCourierClientCard(CourierClientCard courierClientCard) {
        this.courierClientCard = courierClientCard;
    }

    public String getGenerateNumber() {
        return generateNumber;
    }

    public void setGenerateNumber(String generateNumber) {
        this.generateNumber = generateNumber;
    }

    public Money getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(Money priceAmount) {
        this.priceAmount = priceAmount;
    }

    public DateTime getTimeOfLastUpdate() {
        return timeOfLastUpdate;
    }

    public void setTimeOfLastUpdate(DateTime timeOfLastUpdate) {
        this.timeOfLastUpdate = timeOfLastUpdate;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public DateTime getTimeOfRequesting() {
        return timeOfRequesting;
    }

    public void setTimeOfRequesting(DateTime timeOfRequesting) {
        this.timeOfRequesting = timeOfRequesting;
    }

    public PaymentState getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(PaymentState paymentState) {
        this.paymentState = paymentState;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
