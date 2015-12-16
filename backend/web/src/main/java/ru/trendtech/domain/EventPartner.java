package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 29.01.2015.
 */
@Entity
@Table(name = "event_partner")
public class EventPartner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "time_event")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfEvent;

    @Column(name = "annotation")
    private String annotation;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "url_event")
    private String urlEvent;

    @Column(name = "full_address")
    private String fullAddress;

    @Column(name = "url_small_pic")
    private String urlSmallPic;

    @Column(name = "cost")
    private String cost;

    @Column(name = "event_name")
    private String eventName;


    /* новые поля для сайта [24.03.2015] */

    @ElementCollection (fetch = FetchType.EAGER)
    private List<String> photosEventsUrl = new ArrayList<>();

    @Column(name = "from_cost")
    private String fromCost;

    @Column(name = "to_cost")
    private String toCost;

    @OneToOne
    @JoinColumn(name = "partner_account_id")
    private PartnerAccount partner;

    @Column(name = "published", nullable = false, columnDefinition = "BIT DEFAULT 0" , length = 1)
    private boolean published;

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public PartnerAccount getPartner() {
        return partner;
    }

    public void setPartner(PartnerAccount partner) {
        this.partner = partner;
    }

    public List<String> getPhotosEventsUrl() {
        return photosEventsUrl;
    }

    public void setPhotosEventsUrl(List<String> photosEventsUrl) {
        this.photosEventsUrl = photosEventsUrl;
    }

    public String getFromCost() {
        return fromCost;
    }

    public void setFromCost(String fromCost) {
        this.fromCost = fromCost;
    }

    public String getToCost() {
        return toCost;
    }

    public void setToCost(String toCost) {
        this.toCost = toCost;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getUrlSmallPic() {
        return urlSmallPic;
    }

    public void setUrlSmallPic(String urlSmallPic) {
        this.urlSmallPic = urlSmallPic;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getTimeOfEvent() {
        return timeOfEvent;
    }

    public void setTimeOfEvent(DateTime timeOfEvent) {
        this.timeOfEvent = timeOfEvent;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrlEvent() {
        return urlEvent;
    }

    public void setUrlEvent(String urlEvent) {
        this.urlEvent = urlEvent;
    }
}

/*
Создать механизм хранения и отдачи Событий партнеров.
В базе храним дату, краткую аннотация события, адрес, телефон, URL для WebView.
 */
