package ru.trendtech.domain.courier;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import ru.trendtech.domain.Client;

import javax.persistence.*;

/**
 * Created by petr on 24.09.2015.
 */
@Entity
@Table(name = "c_client_card")
public class CourierClientCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "rebill_anchor")
    private String rebillAnchor;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "card_holder")
    private String cardHolder;

    @Column(name = "transaction_id")
    private String transactionID;

    @Column(name = "amount")
    private int amount;

    @Column(name = "time_requesting")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfRequesting;

    @Column(name = "payment_state_card", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PaymentState paymentState = PaymentState.NONE;

    @Column(name = "generate_number")
    private String generateNumber; // id + ":" + "c"

    @Column(name = "code")
    private String code;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "message")
    private String message;

    @Column(name = "security_key")
    private String securityKey;

    @Column(name = "active" , nullable = false, columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean active;

    @Column(name = "is_delete" , nullable = false, columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean isDelete;

    public boolean isDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public PaymentState getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(PaymentState paymentState) {
        this.paymentState = paymentState;
    }

    public String getSecurityKey() {
        return securityKey;
    }

    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getGenerateNumber() {
        return generateNumber;
    }

    public void setGenerateNumber(String generateNumber) {
        this.generateNumber = generateNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
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

    public String getRebillAnchor() {
        return rebillAnchor;
    }

    public void setRebillAnchor(String rebillAnchor) {
        this.rebillAnchor = rebillAnchor;
    }

    public DateTime getTimeOfRequesting() {
        return timeOfRequesting;
    }

    public void setTimeOfRequesting(DateTime timeOfRequesting) {
        this.timeOfRequesting = timeOfRequesting;
    }
}
