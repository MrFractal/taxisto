package ru.trendtech.common.mobileexchange.model.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 24.03.2015.
 */
public class EventPartnerSiteInfo {
    private Long eventId;
    private long timeOfEvent;
    private String annotation;
    private String address;
    private String eventName;
    private List<String> photosEventsPictures = new ArrayList<String>();
    private List<String> photosEventsUrl = new ArrayList<String>();
    private String fromCost;
    private String toCost;
    private long partnerId;
    private boolean published;
    private int typeStateEvent; // 1 - будущие, 2 - прошедшие

    public int getTypeStateEvent() {
        return typeStateEvent;
    }

    public void setTypeStateEvent(int typeStateEvent) {
        this.typeStateEvent = typeStateEvent;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public List<String> getPhotosEventsPictures() {
        return photosEventsPictures;
    }

    public void setPhotosEventsPictures(List<String> photosEventsPictures) {
        this.photosEventsPictures = photosEventsPictures;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public long getTimeOfEvent() {
        return timeOfEvent;
    }

    public void setTimeOfEvent(long timeOfEvent) {
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

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
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

    public long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(long partnerId) {
        this.partnerId = partnerId;
    }
}
