package ru.trendtech.domain;

import javax.persistence.*;

/**
 * Created by petr on 19.09.2014.
 */

@Entity
@Table(name = "item_partners_group")
public class ItemPartnersGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name="";

    @Column(name = "region")
    private String region="";

    @Column(name = "city")
    private String city="";

    @Column(name = "street")
    private String street="";

    @Column(name = "house") // , nullable = false
    private String house="";

    @Column(name = "korpus")
    private String korpus="";

    @Column(name = "phone")
    private String phone="";

    @OneToOne
    @JoinColumn(name = "group_id", nullable = false)
    private PartnersGroup partnersGroup;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getKorpus() {
        return korpus;
    }

    public void setKorpus(String korpus) {
        this.korpus = korpus;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public PartnersGroup getPartnersGroup() {
        return partnersGroup;
    }

    public void setPartnersGroup(PartnersGroup partnersGroup) {
        this.partnersGroup = partnersGroup;
    }
}


