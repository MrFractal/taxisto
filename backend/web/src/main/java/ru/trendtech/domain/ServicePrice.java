package ru.trendtech.domain;

import javax.persistence.*;

@Entity
@Table(name = "services_prices")
public class ServicePrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "services")
    @Enumerated(value = EnumType.STRING)
    private MissionService service;

    @Column(name = "money_currency")
    private String money_currency;

    @Column(name = "money_amount")
    private double money_amount;

    @Column(name = "active_pic_url")
    private String activePicUrl;

    @Column(name = "not_active_pic_url")
    private String notActivePicUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "name")
    private String name;

    @Column(name = "name_v2")
    private String nameV2;

    @Column(name = "active_pic_url_v2")
    private String activePicUrlV2;

    @Column(name = "description_v2")
    private String descriptionV2;

    @Column(name = "sort", columnDefinition = "int default 0")
    private int sort;

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getNameV2() {
        return nameV2;
    }

    public void setNameV2(String nameV2) {
        this.nameV2 = nameV2;
    }

    public String getActivePicUrlV2() {
        return activePicUrlV2;
    }

    public void setActivePicUrlV2(String activePicUrlV2) {
        this.activePicUrlV2 = activePicUrlV2;
    }

    public String getDescriptionV2() {
        return descriptionV2;
    }

    public void setDescriptionV2(String descriptionV2) {
        this.descriptionV2 = descriptionV2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MissionService getService() {
        return service;
    }

    public void setService(MissionService service) {
        this.service = service;
    }

    public String getActivePicUrl() {
        return activePicUrl;
    }

    public void setActivePicUrl(String activePicUrl) {
        this.activePicUrl = activePicUrl;
    }

    public String getNotActivePicUrl() {
        return notActivePicUrl;
    }

    public void setNotActivePicUrl(String notActivePicUrl) {
        this.notActivePicUrl = notActivePicUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoney_currency() {
        return money_currency;
    }

    public void setMoney_currency(String money_currency) {
        this.money_currency = money_currency;
    }

    public double getMoney_amount() {
        return money_amount;
    }

    public void setMoney_amount(double money_amount) {
        this.money_amount = money_amount;
    }
}
