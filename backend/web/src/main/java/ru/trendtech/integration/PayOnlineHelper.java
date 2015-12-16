package ru.trendtech.integration;

/**
 * Created by petr on 14.09.2015.
 */
public class PayOnlineHelper {
    private String transactionId;
    private String operation; // Auth
    private String result;
    private String code;
    private String errorCode = "0";
    private String message;
    private String dateTime;
    private String status;
    private String amount;
    /* for 3dsecure */
    private String pareq;
    private String pd;
    private String acsurl;
    private String threedSecureHtmlPageConfirm;
    private String redirectUrl;

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getThreedSecureHtmlPageConfirm() {
        return threedSecureHtmlPageConfirm;
    }

    public void setThreedSecureHtmlPageConfirm(String threedSecureHtmlPageConfirm) {
        this.threedSecureHtmlPageConfirm = threedSecureHtmlPageConfirm;
    }

    public String getPareq() {
        return pareq;
    }

    public void setPareq(String pareq) {
        this.pareq = pareq;
    }

    public String getPd() {
        return pd;
    }

    public void setPd(String pd) {
        this.pd = pd;
    }

    public String getAcsurl() {
        return acsurl;
    }

    public void setAcsurl(String acsurl) {
        this.acsurl = acsurl;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
