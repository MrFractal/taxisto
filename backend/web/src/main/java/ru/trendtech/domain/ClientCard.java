package ru.trendtech.domain;

import ru.trendtech.domain.billing.Account;

import javax.persistence.*;

/**
 * Created by petr on 14.09.2014.
 */

@Entity
@Table(name = "client_card")
public class ClientCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false) // , nullable = false
    private Client client;

    @Column(name = "binding_id" , unique = true, length = 75)
    private String bindingId;

    @Column(name = "active" , nullable = false, columnDefinition = "BIT DEFAULT 0", length = 1) // , columnDefinition = "boolean default false"
    private Boolean active=false;

    @Column(name = "md_order_number", length = 75) // , unique = true, length = 75
    private String mdOrderNumber="";

    @Column(name = "cardholderName") // , unique = true, length = 75
    private String cardholderName="";

    @Column(name = "merchant_order_number" , length = 75) // , unique = true, length = 75
    private String mrchOrder="";

    @Column(name = "pan") // , unique = true, length = 75
    private String pan="";

    @Column(name = "expiration_year")
    private Integer expirationYear=0;

    @Column(name = "expiration_month")
    private Integer expirationMonth=0;

    @Column(name = "status_delete" , nullable = false, columnDefinition = "BIT DEFAULT 0", length = 1) // , columnDefinition = "boolean default false"
    private boolean statusDelete=false;

    @Column(name = "card_id")
    private Long cardId=0L;

    @Column(name = "payment_status", columnDefinition = "BIT DEFAULT 0", length = 1) // , columnDefinition = "boolean default false"
    private Boolean paymentStatus;

    @Column(name = "payment_description") // , unique = true, length = 75
    private String paymentDescription="";

    @Column(name = "payment_date") // , unique = true, length = 75
    private Long paymentDate;

    @Column(name = "refund_status", columnDefinition = "BIT DEFAULT 0", length = 1) // , columnDefinition = "boolean default false"
    private Boolean refundStatus;

    @Column(name = "refund_description") // , unique = true, length = 75
    private String refundDescription="";

    @Column(name = "refund_date") // , unique = true, length = 75
    private Long refundDate;


    /*
    private Long clientId;
    private String bindingId;
    private String mdOrder;
    private Boolean active;
    private Long cardId;
    private String cardholderName;
    private String pan;
    private Integer expirationYear;
    private Integer expirationMonth;
    private Boolean statusDelete;
    private String mrchOrder;
     */


    public Boolean getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Boolean getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Boolean refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getPaymentDescription() {
        return paymentDescription;
    }

    public void setPaymentDescription(String paymentDescription) {
        this.paymentDescription = paymentDescription;
    }

    public Long getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Long paymentDate) {
        this.paymentDate = paymentDate;
    }


    public String getRefundDescription() {
        return refundDescription;
    }

    public void setRefundDescription(String refundDescription) {
        this.refundDescription = refundDescription;
    }

    public Long getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(Long refundDate) {
        this.refundDate = refundDate;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public String getMrchOrder() {
        return mrchOrder;
    }

    public void setMrchOrder(String mrchOrder) {
        this.mrchOrder = mrchOrder;
    }

    public boolean isStatusDelete() {
        return statusDelete;
    }

    public void setStatusDelete(boolean statusDelete) {
        this.statusDelete = statusDelete;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public Integer getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(Integer expirationYear) {
        this.expirationYear = expirationYear;
    }

    public Integer getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(Integer expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public String getMdOrderNumber() {
        return mdOrderNumber;
    }

    public void setMdOrderNumber(String mdOrderNumber) {
        this.mdOrderNumber = mdOrderNumber;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    public String getBindingId() {
        return bindingId;
    }

    public void setBindingId(String bindingId) {
        this.bindingId = bindingId;
    }
}
