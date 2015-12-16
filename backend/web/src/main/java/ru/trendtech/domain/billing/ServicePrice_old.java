package ru.trendtech.domain.billing;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import ru.trendtech.domain.MissionService;
import ru.trendtech.utils.MoneyUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
/**
 * File created by max on 08/05/2014 14:18.
 */
     /*
       УДАЛИТЬ НАФИГ, УЖЕ ТАКАЯ МОДЕЛЬ ЕСТЬ
     */


@Embeddable
public class ServicePrice_old {
    @Column(name = "auto_class")
    @Enumerated(value = EnumType.STRING)
    private MissionService service;

    @Columns(columns = {@Column(name = "money_currency"), @Column(name = "money_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money price = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    public MissionService getService() {
        return service;
    }

    public void setService(MissionService service) {
        this.service = service;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }
}
