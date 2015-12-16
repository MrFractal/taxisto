package ru.trendtech.domain.courier;

import ru.trendtech.domain.Location;

import javax.persistence.*;

/**
 * Created by petr on 20.08.2015.
 */
@Entity
@Table(name = "c_store_address")
public class StoreAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Embedded
    private Location storeLocation = new Location();

    @OneToOne
    @JoinColumn(name = "c_store_id", nullable = false)
    private Store store;

    @Column(name = "phone")
    private String phone;

    @Column(name = "comment")
    private String comment;

    @Transient
    private double distance;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Location getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(Location storeLocation) {
        this.storeLocation = storeLocation;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
