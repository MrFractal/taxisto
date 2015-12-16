package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 05.11.2014.
 */
public class MdOrderInfo {
    private Long id;
    private Long clientId;
    private Long missionId;
    private String bindingId;
    private String mdOrderNumber;
    private int sum;
    private long timeOfInsert;
    private Long clientCardId;
    private Boolean paymentStatus;
    private String paymentDescription="";
    private Long paymentDate;
    private Boolean refundStatus;
    private String refundDescription="";
    private Long refundDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
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

    public long getTimeOfInsert() {
        return timeOfInsert;
    }

    public void setTimeOfInsert(long timeOfInsert) {
        this.timeOfInsert = timeOfInsert;
    }

    public Long getClientCardId() {
        return clientCardId;
    }

    public void setClientCardId(Long clientCardId) {
        this.clientCardId = clientCardId;
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
}
