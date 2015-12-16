package ru.trendtech.domain.courier;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 15.09.2015.
 */
@Entity
@Table(name = "c_late_order")
public class LateOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "c_order_id", nullable = false)
    private Order order;

    @Column(name = "time_requesting")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfRequesting;

    @Column(name = "minutes_late")
    private int minutesLate; // min


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

    public DateTime getTimeOfRequesting() {
        return timeOfRequesting;
    }

    public void setTimeOfRequesting(DateTime timeOfRequesting) {
        this.timeOfRequesting = timeOfRequesting;
    }

    public int getMinutesLate() {
        return minutesLate;
    }

    public void setMinutesLate(int minutesLate) {
        this.minutesLate = minutesLate;
    }
}
