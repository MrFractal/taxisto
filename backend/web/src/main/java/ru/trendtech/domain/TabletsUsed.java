package ru.trendtech.domain;

import org.hibernate.annotations.Table;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 22.06.2015.
 */
@Entity
@javax.persistence.Table(name = "tablets_used")
public class TabletsUsed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start_period")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime startUsed;

    @Column(name = "end_period")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime endUsed;

    @OneToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @OneToOne
    @JoinColumn(name = "tablet_id", nullable = false)
    private Tablet tablet;

    public Tablet getTablet() {
        return tablet;
    }

    public void setTablet(Tablet tablet) {
        this.tablet = tablet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getStartUsed() {
        return startUsed;
    }

    public void setStartUsed(DateTime startUsed) {
        this.startUsed = startUsed;
    }

    public DateTime getEndUsed() {
        return endUsed;
    }

    public void setEndUsed(DateTime endUsed) {
        this.endUsed = endUsed;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
//if(info.getTabletInfo()!=null && info.getTabletInfo().getId()!=0){
//        driver.setTablet(tabletRepository.findOne(info.getTabletInfo().getId()));
//        }