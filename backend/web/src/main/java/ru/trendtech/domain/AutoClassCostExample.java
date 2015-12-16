package ru.trendtech.domain;

import javax.persistence.*;

/**
 * Created by petr on 19.06.2015.
 */
@Entity
@Table(name = "auto_class_cost_example")
public class AutoClassCostExample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "auto_class", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AutoClass autoClass;

    @Column(name = "address_from")
    private String addressFrom;

    @Column(name = "address_to")
    private String addressTo;

    @Column(name = "cost")
    private String cost;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AutoClass getAutoClass() {
        return autoClass;
    }

    public void setAutoClass(AutoClass autoClass) {
        this.autoClass = autoClass;
    }

    public String getAddressFrom() {
        return addressFrom;
    }

    public void setAddressFrom(String addressFrom) {
        this.addressFrom = addressFrom;
    }

    public String getAddressTo() {
        return addressTo;
    }

    public void setAddressTo(String addressTo) {
        this.addressTo = addressTo;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
