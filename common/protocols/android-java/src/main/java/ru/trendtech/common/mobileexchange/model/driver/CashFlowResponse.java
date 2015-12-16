package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class CashFlowResponse {
    private double incomeTotal;
    private double expenseTotal;
    private double balanceTotal;
    private List<CashInfo> cashInfo;

    public List<CashInfo> getCashInfo() {
        return cashInfo;
    }

    public void setCashInfo(List<CashInfo> cashInfo) {
        this.cashInfo = cashInfo;
    }

    public double getBalanceTotal() {
        return balanceTotal;
    }

    public void setBalanceTotal(double balanceTotal) {
        this.balanceTotal = balanceTotal;
    }

    public double getExpenseTotal() {
        return expenseTotal;
    }

    public void setExpenseTotal(double expenseTotal) {
        this.expenseTotal = expenseTotal;
    }

    public double getIncomeTotal() {
        return incomeTotal;
    }

    public void setIncomeTotal(double incomeTotal) {
        this.incomeTotal = incomeTotal;
    }
}
