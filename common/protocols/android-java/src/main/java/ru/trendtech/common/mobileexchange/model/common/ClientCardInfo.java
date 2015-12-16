package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 16.09.2014.
 */


public class ClientCardInfo {

    private Long id;
    private Long clientId;
    private String bindingId;
    private Boolean active;
    private String mdOrderNumber;
    private String cardholderName;
    private String pan;
    private Integer expirationYear;
    private Integer expirationMonth;
    private boolean statusDelete;
    private String mrchOrder;

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

    public String getBindingId() {
        return bindingId;
    }

    public void setBindingId(String bindingId) {
        this.bindingId = bindingId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getMdOrderNumber() {
        return mdOrderNumber;
    }

    public void setMdOrderNumber(String mdOrderNumber) {
        this.mdOrderNumber = mdOrderNumber;
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
}
