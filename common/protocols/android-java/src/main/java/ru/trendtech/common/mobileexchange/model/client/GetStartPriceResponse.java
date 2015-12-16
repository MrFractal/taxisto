package ru.trendtech.common.mobileexchange.model.client;

public class GetStartPriceResponse {
    private int sum;
    private int sumWithOption;

    public int getSumWithOption() {
        return sumWithOption;
    }

    public void setSumWithOption(int sumWithOption) {
        this.sumWithOption = sumWithOption;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
