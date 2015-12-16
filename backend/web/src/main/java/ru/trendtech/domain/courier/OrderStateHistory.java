package ru.trendtech.domain.courier;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 15.10.2015.
 */
@Entity
@Table(name = "c_order_state_history")
public class OrderStateHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "c_order_id", nullable = false)
    private Order order;

    @Column(name = "state", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Order.State state;

    @Column(name = "time_requesting")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfChange;


    public OrderStateHistory(){

    }

    public OrderStateHistory(Order order, Order.State state, DateTime timeOfChange) {
        this.order = order;
        this.state = state;
        this.timeOfChange = timeOfChange;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Order.State getState() {
        return state;
    }

    public void setState(Order.State state) {
        this.state = state;
    }

    public DateTime getTimeOfChange() {
        return timeOfChange;
    }

    public void setTimeOfChange(DateTime timeOfChange) {
        this.timeOfChange = timeOfChange;
    }
}
