package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 05.09.2014.
 */

public class GeneratePromoCodesRequest {
    private int countSymbols=0;
    private int countPromoCode=0;
    private int amount=0;
    private int availableUsedCount=0;
    private String channel;
    /* количество дней жизни акционног предложения с момента его активации
       т.е.: пользователь активирует промокод, включается акция (например, тариф бонус), которая будет
       активна в течении lifetimeDaysAfterActivation после активации данного промокода
    */
    private int lifetimeDaysAfterActivation=0;

    public int getLifetimeDaysAfterActivation() {
        return lifetimeDaysAfterActivation;
    }

    public void setLifetimeDaysAfterActivation(int lifetimeDaysAfterActivation) {
        this.lifetimeDaysAfterActivation = lifetimeDaysAfterActivation;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getAvailableUsedCount() {
        return availableUsedCount;
    }

    public void setAvailableUsedCount(int availableUsedCount) {
        this.availableUsedCount = availableUsedCount;
    }

    public int getCountPromoCode() {
        return countPromoCode;
    }

    public void setCountPromoCode(int countPromoCode) {
        this.countPromoCode = countPromoCode;
    }

    public int getCountSymbols() {
        return countSymbols;
    }

    public void setCountSymbols(int countSymbols) {
        this.countSymbols = countSymbols;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
