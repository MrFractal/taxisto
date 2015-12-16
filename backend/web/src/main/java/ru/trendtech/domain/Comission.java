package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 17.12.2014.
 */

@Entity
@Table(name = "comission")
public class Comission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start_time")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime startTime;

    @Column(name = "end_time")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime endTime;

    @Column(name = "comission_amount", nullable = false)
    private int comissionAmount=0;

    @Column(name = "comission_type", nullable = false)
    private int comissionType;

    @Column(name = "object_id")
    private Long objectId; // id taxopark or id driver

//    @OneToOne
//    @JoinColumn(name = "taxopark_id", nullable = false, unique = true)
//    private TaxoparkPartners taxoparkPartners;

    @Column(name = "update_time")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    public int getComissionAmount() {
        return comissionAmount;
    }

    public void setComissionAmount(int comissionAmount) {
        this.comissionAmount = comissionAmount;
    }

    public int getComissionType() {
        return comissionType;
    }

    public void setComissionType(int comissionType) {
        this.comissionType = comissionType;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public DateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(DateTime updateTime) {
        this.updateTime = updateTime;
    }
}



/*
        Процент задается в разрезе:
        процент комиссии (обязательный параметр. может быть и 0),
        таксопрак,
        дата/время начала действия,
        дата/время окончания действия.
*/