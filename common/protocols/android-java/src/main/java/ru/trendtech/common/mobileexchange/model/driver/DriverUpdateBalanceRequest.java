package ru.trendtech.common.mobileexchange.model.driver;

import java.math.BigDecimal;

/**
 * Created by petr on 21.08.14.
 */

public class DriverUpdateBalanceRequest {
    private Long driverId;
    private double amountOfMoney;
    private Long missionId;
    private int operation;
    private String security_token;
    private String comment;
    private Long articleAdjustmentId;


    public Long getArticleAdjustmentId() {
        return articleAdjustmentId;
    }

    public void setArticleAdjustmentId(Long articleAdjustmentId) {
        this.articleAdjustmentId = articleAdjustmentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public double getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(double amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }
}


//        Таблицы для изменения:
//        1. driver_cash_flow
//        Сюда пишем sum (в копейках), driverId, dateOperation, missionId (если есть), operation (1).
//        2. account
//        Уменьшаем money_amount (в копейках)