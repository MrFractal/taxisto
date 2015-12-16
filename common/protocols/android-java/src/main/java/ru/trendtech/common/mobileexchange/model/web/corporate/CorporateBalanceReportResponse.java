package ru.trendtech.common.mobileexchange.model.web.corporate;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 27.04.2015.
 */
public class CorporateBalanceReportResponse extends ErrorCodeHelper {
    private List<CorporateBalanceReportInfo> balanceReportInfos = new ArrayList<CorporateBalanceReportInfo>();

    public List<CorporateBalanceReportInfo> getBalanceReportInfos() {
        return balanceReportInfos;
    }

    public void setBalanceReportInfos(List<CorporateBalanceReportInfo> balanceReportInfos) {
        this.balanceReportInfos = balanceReportInfos;
    }
}
/*
Колонки:
1. Клиент (главная учетка)
2. Нач. остаток (сумма)
3. Приход (сумма)
3. Расход (сумма)
3. Кон. остаток (сумма)
 */
