package ru.trendtech.domain.courier;

import com.google.common.base.Objects;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import ru.trendtech.utils.MoneyUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by petr on 20.08.2015.
 */
@Entity
@Table(name = "c_item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Columns(columns = {@Column(name = "default_item_price_currency"), @Column(name = "default_item_price")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money defaultItemPrice = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ItemType itemType;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "c_item_properties", joinColumns = {@JoinColumn(name = "c_item_id")})
    private List<Long> itemPropertyId = new ArrayList<>();


    @Column(name = "active" , nullable = false, columnDefinition = "BIT DEFAULT 1", length = 1)
    private boolean active; // 0 - какой-то товар мы больше не возим (мясо, например)


    public enum ItemType {
        PRODUCT,
        SERVICE,
    }


    public List<Long> getItemPropertyId() {
        return itemPropertyId;
    }

    public void setItemPropertyId(List<Long> itemPropertyId) {
        this.itemPropertyId = itemPropertyId;
    }

    public Money getDefaultItemPrice() {
        return defaultItemPrice;
    }

    public void setDefaultItemPrice(Money defaultItemPrice) {
        this.defaultItemPrice = defaultItemPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return Objects.equal(isActive(), item.isActive()) &&
                Objects.equal(getId(), item.getId()) &&
                Objects.equal(getItemName(), item.getItemName()) &&
                Objects.equal(getItemType(), item.getItemType());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getItemName(), getItemType(), isActive());
    }
}
