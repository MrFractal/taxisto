package ru.trendtech.domain.courier;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import javax.persistence.*;

/**
 * Created by petr on 20.08.2015.
 */
@Entity
@Table(name = "c_comment_order")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "comment")
    private String comment;

    @OneToOne
    @JoinColumn(name = "c_order_id")
    private Order order;

    @Column(name = "time_comment")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfComment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public DateTime getTimeOfComment() {
        return timeOfComment;
    }

    public void setTimeOfComment(DateTime timeOfComment) {
        this.timeOfComment = timeOfComment;
    }
}
