package ru.trendtech.utils;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Created by ivanenok on 4/13/2014.
 */
public class MoneyUtils {
    public static final CurrencyUnit DEFAULT_CURRENCY = CurrencyUnit.getInstance("RUB");
    public static final CurrencyUnit BONUSES_CURRENCY = CurrencyUnit.registerCurrency("BNS", 995, 2, Arrays.asList("RU"), true);

    public static Money getMoney(CurrencyUnit currency, float money) {
        return Money.of(currency, new BigDecimal(money));
    }

    public static Money getMoney(float money) {
        return Money.of(DEFAULT_CURRENCY, new BigDecimal(money));
    }

    public static Money getRubles(double amount) {
        return Money.of(DEFAULT_CURRENCY, new BigDecimal(amount));
    }

    public static Money getBonuses(double amount) {
        return Money.of(BONUSES_CURRENCY, new BigDecimal(amount));
    }
}
