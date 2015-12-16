package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 30.07.2015.
 */
public class TaxoparkUpdateBalanceRequest {
    private String security_token;
    private Long taxoparkId;
    private int amountOfMoney;
    private Long missionId;
    private int operation;
    private String comment;
    private Long articleAdjustmentId;

    public Long getArticleAdjustmentId() {
        return articleAdjustmentId;
    }

    public void setArticleAdjustmentId(Long articleAdjustmentId) {
        this.articleAdjustmentId = articleAdjustmentId;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public Long getTaxoparkId() {
        return taxoparkId;
    }

    public void setTaxoparkId(Long taxoparkId) {
        this.taxoparkId = taxoparkId;
    }

    public int getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(int amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
