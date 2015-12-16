package ru.trendtech.common.mobileexchange.model.driver;

public class CashInfo {
    private long operationDate;
    private long missionId;
    private double income;
    private String addressFrom;
    private String addressTo;
    private double expense;

    public long getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(long operationDate) {
        this.operationDate = operationDate;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public String getAddressFrom() {
        return addressFrom;
    }

    public void setAddressFrom(String addressFrom) {
        this.addressFrom = addressFrom;
    }

    public String getAddressTo() {
        return addressTo;
    }

    public void setAddressTo(String addressTo) {
        this.addressTo = addressTo;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }
}
