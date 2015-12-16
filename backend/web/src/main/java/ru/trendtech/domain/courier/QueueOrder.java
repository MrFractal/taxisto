package ru.trendtech.domain.courier;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.Mission;

import javax.persistence.*;

/**
 * Created by petr on 16.09.2015.
 */
@Entity
@Table(name = "c_queue_order")
public class QueueOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @OneToOne
    @JoinColumn(name = "c_order_id", nullable = false, unique = true)
    private Order order;

    @Column(name = "time_assigning")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfAssigning;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public DateTime getTimeOfAssigning() {
        return timeOfAssigning;
    }

    public void setTimeOfAssigning(DateTime timeOfAssigning) {
        this.timeOfAssigning = timeOfAssigning;
    }
}
