package ru.trendtech.domain.courier;

import javax.persistence.*;

/**
 * Created by petr on 20.08.2015.
 */
@Entity
@Table(name = "c_store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "store_name")
    private String storeName;


    // todo: возможно лучше убрать это отсюда и доставать адреса отдельным запросом
//    @OneToMany
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @JoinColumn(name = "store_id", insertable = true, updatable = true)
//    private Set<StoreAddress> storeAddresses = new HashSet<>();



    /* todo: возможно лучше убрать это отсюда и доставать адреса отдельным запросом
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "store_id", insertable = true, updatable = true)
    private Set<StoreWorkTime> storeWorkTimes = new HashSet<>();
    */


    @Column(name = "active" , nullable = false, columnDefinition = "BIT DEFAULT 1", length = 1)
    private boolean active;


    /*
    public Set<StoreWorkTime> getStoreWorkTimes() {
        return storeWorkTimes;
    }

    public void setStoreWorkTimes(Set<StoreWorkTime> storeWorkTimes) {
        this.storeWorkTimes = storeWorkTimes;
    }

    */

//    public Set<StoreAddress> getStoreAddresses() {
//        return storeAddresses;
//    }
//
//    public void setStoreAddresses(Set<StoreAddress> storeAddresses) {
//        this.storeAddresses = storeAddresses;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
