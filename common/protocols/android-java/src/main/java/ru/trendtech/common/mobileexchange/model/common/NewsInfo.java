package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 01.12.2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsInfo {
    private Long id;
    private String textNews;
    private long timeOfStarting;
    private long timeOfFinishing;
    private String urlNews;
    private String title;
    private boolean isReading=false;

    public boolean isReading() {
        return isReading;
    }

    public void setReading(boolean isReading) {
        this.isReading = isReading;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTextNews() {
        return textNews;
    }

    public void setTextNews(String textNews) {
        this.textNews = textNews;
    }

    public long getTimeOfStarting() {
        return timeOfStarting;
    }

    public void setTimeOfStarting(long timeOfStarting) {
        this.timeOfStarting = timeOfStarting;
    }

    public long getTimeOfFinishing() {
        return timeOfFinishing;
    }

    public void setTimeOfFinishing(long timeOfFinishing) {
        this.timeOfFinishing = timeOfFinishing;
    }

    public String getUrlNews() {
        return urlNews;
    }

    public void setUrlNews(String urlNews) {
        this.urlNews = urlNews;
    }
}
