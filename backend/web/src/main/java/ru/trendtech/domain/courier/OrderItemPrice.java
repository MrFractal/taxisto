package ru.trendtech.domain.courier;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import ru.trendtech.utils.MoneyUtils;

import javax.persistence.*;

/**
 * Created by petr on 21.08.2015.
 */
@Entity
@Table(name = "c_order_item_price")
public class OrderItemPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "c_item_price_id", nullable = false)
    private ItemPrice itemPrice;

    @Columns(columns = {@Column(name = "price_on_day_of_order_currency"), @Column(name = "price_on_day_of_order")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money priceOnDayOfOrder = Money.zero(MoneyUtils.DEFAULT_CURRENCY); // стоимость в день оформления заказа за еденицу товара (пусто если услуга)

    @Column(name = "count_item")
    private int countItem;

    @OneToOne
    @JoinColumn(name = "c_order_id", nullable = false)
    private Order order;

    @Column(name = "order_number", nullable = false)
    private int orderNumber;

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Money getPriceOnDayOfOrder() {
        return priceOnDayOfOrder;
    }

    public void setPriceOnDayOfOrder(Money priceOnDayOfOrder) {
        this.priceOnDayOfOrder = priceOnDayOfOrder;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ItemPrice getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(ItemPrice itemPrice) {
        this.itemPrice = itemPrice;
    }


    public int getCountItem() {
        return countItem;
    }

    public void setCountItem(int countItem) {
        this.countItem = countItem;
    }
}
