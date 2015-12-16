package ru.trendtech.domain.courier;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 16.09.2015.
 */
@Entity
@Table(name = "c_remind_order")
public class RemindOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "time_remind")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfRemind;

    @Column(name = "time_confirm")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfConfirm;

    @OneToOne
    @JoinColumn(name = "c_order_id", nullable = false)
    private Order order;

    @Column(name = "remind_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RemindType remindType;

    public RemindType getRemindType() {
        return remindType;
    }

    public void setRemindType(RemindType remindType) {
        this.remindType = remindType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getTimeOfRemind() {
        return timeOfRemind;
    }

    public void setTimeOfRemind(DateTime timeOfRemind) {
        this.timeOfRemind = timeOfRemind;
    }

    public DateTime getTimeOfConfirm() {
        return timeOfConfirm;
    }

    public void setTimeOfConfirm(DateTime timeOfConfirm) {
        this.timeOfConfirm = timeOfConfirm;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


    public enum RemindType {
        FIRST,
        SECOND,
    }
}
