package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 22.06.2015.
 */
@Entity
@Table(name="tablet")
public class Tablet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "model")
    private String model;

    @Column(name = "imei", unique = true)
    private String imeiNumber;

    @Column(name = "tablet_state")
    @Enumerated(value = EnumType.STRING)
    private TabletState tabletState;

    @Column(name = "time_update")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfUpdate;

    @Column(name = "is_own", columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean own;

    @Column(name = "phone")
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isOwn() {
        return own;
    }

    public void setOwn(boolean own) {
        this.own = own;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public TabletState getTabletState() {
        return tabletState;
    }

    public void setTabletState(TabletState tabletState) {
        this.tabletState = tabletState;
    }

    public DateTime getTimeOfUpdate() {
        return timeOfUpdate;
    }

    public void setTimeOfUpdate(DateTime timeOfUpdate) {
        this.timeOfUpdate = timeOfUpdate;
    }


}


