package ru.trendtech.domain.courier;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import ru.trendtech.utils.MoneyUtils;

import javax.persistence.*;

/**
 * Created by petr on 03.09.2015.
 */
@Entity
@Table(name = "c_client_item")
public class ClientItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /* выбрал из списка */
    @OneToOne
    @JoinColumn(name = "c_item_id")
    private Item item;

    /* название итема, которого нет в списке */
    @Column(name = "undefined_item_name")
    private String undefinedItemName;

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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getUndefinedItemName() {
        return undefinedItemName;
    }

    public void setUndefinedItemName(String undefinedItemName) {
        this.undefinedItemName = undefinedItemName;
    }

    public int getCountItem() {
        return countItem;
    }

    public void setCountItem(int countItem) {
        this.countItem = countItem;
    }
}
