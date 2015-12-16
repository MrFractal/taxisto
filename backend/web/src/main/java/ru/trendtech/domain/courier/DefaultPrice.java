package ru.trendtech.domain.courier;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import ru.trendtech.utils.MoneyUtils;

import javax.persistence.*;

/**
 * Created by petr on 20.08.2015.
 * здесь удет лежать стоимость минималки в зависимости от типа заказа, а так же информация о километраже
 */

@Entity
@Table(name = "c_default_prices")
public class DefaultPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Columns(columns = {@Column(name = "minimal_price_currency"), @Column(name = "minimal_price")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money minimalPrice = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    @Columns(columns = {@Column(name = "comission_price_currency"), @Column(name = "comission_price")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money comissionPrice = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    @Columns(columns = {@Column(name = "to_courier_price_currency"), @Column(name = "to_courier_price")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money toCourierPrice = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    @Columns(columns = {@Column(name = "fine_amount_currency"), @Column(name = "fine_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money fineAmount = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    @Columns(columns = {@Column(name = "per_km_currency"), @Column(name = "per_km_price")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money perKmPrice = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    @Columns(columns = {@Column(name = "additional_address_price_currency"), @Column(name = "additional_address_price")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money additionalAddressPrice = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    /* забираем себе за доп. адрес */
    @Columns(columns = {@Column(name = "commission_address_currency"), @Column(name = "commission_address_price")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money commissionForAddressPrice = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    /* курьеру за доп. адрес */
    @Columns(columns = {@Column(name = "to_courier_address_currency"), @Column(name = "to_courier_address_price")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money toCourierForAddressPrice = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    @Column(name = "km_included")
    private int kmIncluded;

    @Columns(columns = {@Column(name = "order_processing_currency"), @Column(name = "order_processing_price")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money orderProcessingPrice = Money.zero(MoneyUtils.DEFAULT_CURRENCY); // стоимость обработки заказа

    @Column(name = "order_type", nullable = false, unique = true)
    @Enumerated(value = EnumType.STRING)
    private OrderType orderType;

    @Column(name = "active", columnDefinition = "BIT DEFAULT 1", length = 1)
    private boolean active;


    public Money getCommissionForAddressPrice() {
        return commissionForAddressPrice;
    }

    public void setCommissionForAddressPrice(Money commissionForAddressPrice) {
        this.commissionForAddressPrice = commissionForAddressPrice;
    }

    public Money getToCourierForAddressPrice() {
        return toCourierForAddressPrice;
    }

    public void setToCourierForAddressPrice(Money toCourierForAddressPrice) {
        this.toCourierForAddressPrice = toCourierForAddressPrice;
    }

    public Money getAdditionalAddressPrice() {
        return additionalAddressPrice;
    }

    public void setAdditionalAddressPrice(Money additionalAddressPrice) {
        this.additionalAddressPrice = additionalAddressPrice;
    }

    public Money getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(Money fineAmount) {
        this.fineAmount = fineAmount;
    }

    public Money getComissionPrice() {
        return comissionPrice;
    }

    public void setComissionPrice(Money comissionPrice) {
        this.comissionPrice = comissionPrice;
    }

    public Money getToCourierPrice() {
        return toCourierPrice;
    }

    public void setToCourierPrice(Money toCourierPrice) {
        this.toCourierPrice = toCourierPrice;
    }

    public int getKmIncluded() {
        return kmIncluded;
    }

    public void setKmIncluded(int kmIncluded) {
        this.kmIncluded = kmIncluded;
    }

    public Money getMinimalPrice() {
        return minimalPrice;
    }

    public void setMinimalPrice(Money minimalPrice) {
        this.minimalPrice = minimalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Money getPerKmPrice() {
        return perKmPrice;
    }

    public void setPerKmPrice(Money perKmPrice) {
        this.perKmPrice = perKmPrice;
    }

    public Money getOrderProcessingPrice() {
        return orderProcessingPrice;
    }

    public void setOrderProcessingPrice(Money orderProcessingPrice) {
        this.orderProcessingPrice = orderProcessingPrice;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
