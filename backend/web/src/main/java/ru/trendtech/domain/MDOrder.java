package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 14.09.2014.
 */

@Entity
@Table(name = "md_order")
public class MDOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToOne
    @JoinColumn(name = "mission_id", unique = true, nullable = false)
    private Mission mission;

    @Column(name = "binding_id") // , unique = true, length = 75
    private String bindingId;

    @Column(name = "md_order_number") // , unique = true, length = 75 / , unique = true, length = 75, nullable = true
    private String mdOrderNumber;

    @Column(name = "sum")
    private int sum=0;

    @Column(name = "time_insert")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfInsert;

    @OneToOne
    @JoinColumn(name = "client_card_id")
    private ClientCard clientCard;

    @Column(name = "corporate_card", columnDefinition = "BIT DEFAULT 0", length = 1)
    private Boolean corporateCard;

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

    @OneToOne
    @JoinColumn(name = "tip_percent_id")
    private TipPercent tipPercent;

    @Column(name = "sum_with_tip")
    private Integer sumWithTip=0;

    public Boolean getCorporateCard() {
        return corporateCard;
    }

    public void setCorporateCard(Boolean corporateCard) {
        this.corporateCard = corporateCard;
    }

    public Integer getSumWithTip() {
        return sumWithTip;
    }

    public void setSumWithTip(Integer sumWithTip) {
        this.sumWithTip = sumWithTip;
    }

    public TipPercent getTipPercent() {
        return tipPercent;
    }

    public void setTipPercent(TipPercent tipPercent) {
        this.tipPercent = tipPercent;
    }

    public Boolean getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
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

    public Boolean getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Boolean refundStatus) {
        this.refundStatus = refundStatus;
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

    public ClientCard getClientCard() {
        return clientCard;
    }

    public void setClientCard(ClientCard clientCard) {
        this.clientCard = clientCard;
    }

    public DateTime getTimeOfInsert() {
        return timeOfInsert;
    }

    public void setTimeOfInsert(DateTime timeOfInsert) {
        this.timeOfInsert = timeOfInsert;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
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

    public String getMdOrderNumber() {
        return mdOrderNumber;
    }

    public void setMdOrderNumber(String mdOrderNumber) {
        this.mdOrderNumber = mdOrderNumber;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
