package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 20.03.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverCorrectionInfo {
    private long id;
    private long driverCashFlowId;
    private long webUserId;
    private String comments;
    private int balanceBefore;
    private int balanceAfter;
    private String article;
    private long dateCorrection;
    private long driverId;
    private String firstName;
    private String lastName;
    private int correctionAmount;
    private String firstNameWeb;
    private String lastNameWeb;

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getCorrectionAmount() {
        return correctionAmount;
    }

    public void setCorrectionAmount(int correctionAmount) {
        this.correctionAmount = correctionAmount;
    }

    public String getFirstNameWeb() {
        return firstNameWeb;
    }

    public void setFirstNameWeb(String firstNameWeb) {
        this.firstNameWeb = firstNameWeb;
    }

    public String getLastNameWeb() {
        return lastNameWeb;
    }

    public void setLastNameWeb(String lastNameWeb) {
        this.lastNameWeb = lastNameWeb;
    }

    public long getDateCorrection() {
        return dateCorrection;
    }

    public void setDateCorrection(long dateCorrection) {
        this.dateCorrection = dateCorrection;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDriverCashFlowId() {
        return driverCashFlowId;
    }

    public void setDriverCashFlowId(long driverCashFlowId) {
        this.driverCashFlowId = driverCashFlowId;
    }

    public long getWebUserId() {
        return webUserId;
    }

    public void setWebUserId(long webUserId) {
        this.webUserId = webUserId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getBalanceBefore() {
        return balanceBefore;
    }

    public void setBalanceBefore(int balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public int getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(int balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }
}
