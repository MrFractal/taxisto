package ru.trendtech.domain.courier;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import org.joda.time.DateTime;
import ru.trendtech.utils.MoneyUtils;

import javax.persistence.*;

/**
 * Created by petr on 20.08.2015.
 */

/*
  айди, итем айди, финиш прайс прайс, дейт
*/

@Entity
@Table(name = "c_item_price")
public class ItemPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "c_item_id", nullable = false)
    private Item item;

    @OneToOne
    @JoinColumn(name = "c_store_address_id")
    private StoreAddress storeAddress; // если это услуга, то магазина может и не быть

    @Columns(columns = {@Column(name = "item_price_currency"), @Column(name = "item_price")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money price = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    @Column(name = "time_finish_pricing")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfFinishPricing; // здесь будет лежать последняя актуальная стоимость по магазину

    @Column(name = "active" , nullable = false, columnDefinition = "BIT DEFAULT 1", length = 1)
    private boolean active; // 0 - данный товар больше не продается в этом магазине

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public StoreAddress getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(StoreAddress storeAddress) {
        this.storeAddress = storeAddress;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    public DateTime getTimeOfFinishPricing() {
        return timeOfFinishPricing;
    }

    public void setTimeOfFinishPricing(DateTime timeOfFinishPricing) {
        this.timeOfFinishPricing = timeOfFinishPricing;
    }
}
