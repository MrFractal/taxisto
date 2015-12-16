package ru.trendtech.common.mobileexchange.model.web.corporate;

import ru.trendtech.common.mobileexchange.model.common.ClientInfoARM;

/**
 * Created by petr on 27.04.2015.
 */
public class CorporateBalanceReportInfo {
    private ClientInfoARM clientInfoARM;
    private int startBalanceAmount;
    private int increaseAmount;
    private int decrease;
    private int endBalanceAmount;

    public ClientInfoARM getClientInfoARM() {
        return clientInfoARM;
    }

    public void setClientInfoARM(ClientInfoARM clientInfoARM) {
        this.clientInfoARM = clientInfoARM;
    }

    public int getStartBalanceAmount() {
        return startBalanceAmount;
    }

    public void setStartBalanceAmount(int startBalanceAmount) {
        this.startBalanceAmount = startBalanceAmount;
    }

    public int getIncreaseAmount() {
        return increaseAmount;
    }

    public void setIncreaseAmount(int increaseAmount) {
        this.increaseAmount = increaseAmount;
    }

    public int getDecrease() {
        return decrease;
    }

    public void setDecrease(int decrease) {
        this.decrease = decrease;
    }

    public int getEndBalanceAmount() {
        return endBalanceAmount;
    }

    public void setEndBalanceAmount(int endBalanceAmount) {
        this.endBalanceAmount = endBalanceAmount;
    }
}
/*
Колонки:
1. Клиент (главная учетка)
2. Нач. остаток (сумма)
3. Приход (сумма)
4. Расход (сумма)
5. Кон. остаток (сумма)
 */