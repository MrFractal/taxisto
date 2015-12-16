package ru.trendtech.common.mobileexchange.model.web.corporate;

/**
 * Created by petr on 07.04.2015.
 */
public class UpdateCorporateClientBalanceRequest {
    private Long mainClientId;
    private int amountOfMoney;
    private int operation;
    private String security_token;
    private String comment;
    private Long articleId;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getMainClientId() {
        return mainClientId;
    }

    public void setMainClientId(Long mainClientId) {
        this.mainClientId = mainClientId;
    }

    public int getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(int amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }


    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
