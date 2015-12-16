package ru.trendtech.domain.courier;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import org.joda.time.DateTime;
import ru.trendtech.domain.Client;
import ru.trendtech.utils.MoneyUtils;

import javax.persistence.*;

/**
 * Created by petr on 18.09.2015.
 */
@Entity
@Table(name = "c_price_changes")
public class PriceChanges {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "change_amount")
    private int changeAmount = 0;

    @OneToOne
    @JoinColumn(name = "c_order_payment_id")
    private OrderPayment orderPayment;

    @Column(name = "time_requesting")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfRequesting;

    @Column(name = "change_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ChangeType changeType = ChangeType.UNKNOWN;

    @OneToOne
    @JoinColumn(name = "c_order_id", nullable = false)
    private Order order;


    public static enum ChangeType {
        UNKNOWN,
        UP,
        DOWN
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(int changeAmount) {
        this.changeAmount = changeAmount;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    public DateTime getTimeOfRequesting() {
        return timeOfRequesting;
    }

    public void setTimeOfRequesting(DateTime timeOfRequesting) {
        this.timeOfRequesting = timeOfRequesting;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public OrderPayment getOrderPayment() {
        return orderPayment;
    }

    public void setOrderPayment(OrderPayment orderPayment) {
        this.orderPayment = orderPayment;
    }
}
