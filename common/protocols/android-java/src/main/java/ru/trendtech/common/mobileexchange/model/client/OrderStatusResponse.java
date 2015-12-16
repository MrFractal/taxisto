package ru.trendtech.common.mobileexchange.model.client;

public class OrderStatusResponse {
//{"expiration":"201512",
// "cardholderName":"Asd asd",
// "depositAmount":100,
// "currency":"810",
// "approvalCode":"123456",
// "authCode":2,"clientId":"1",
// "bindingId":"9460b7ac-f278-4362-a1e1-280854991872",
// "ErrorCode":"0",
// "ErrorMessage":"Успешно",
// "OrderStatus":2,
// "OrderNumber":"card_1141095964428",
// "Pan":"555555**5599",
// "Amount":100,"Ip":"46.241.87.74"}

    private String depositAmount;
    private String errorCode;
    private String errorMessage;
    private String orderStatus;
    private String cardholderName;
    private String pan;
    private String expiration;
    private String bindingId;


    public String getBindingId() {
        return bindingId;
    }

    public void setBindingId(String bindingId) {
        this.bindingId = bindingId;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }
}
