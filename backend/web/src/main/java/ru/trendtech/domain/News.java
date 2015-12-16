package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 01.12.2014.
 */
@Entity
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "text_news")
    private String textNews;

    @Column(name = "title")
    private String title;

    @Column(name = "time_starting")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfStarting;

    @Column(name = "time_finishing")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfFinishing;

    @Column(name = "url_news")
    private String urlNews;

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

    public DateTime getTimeOfStarting() {
        return timeOfStarting;
    }

    public void setTimeOfStarting(DateTime timeOfStarting) {
        this.timeOfStarting = timeOfStarting;
    }

    public DateTime getTimeOfFinishing() {
        return timeOfFinishing;
    }

    public void setTimeOfFinishing(DateTime timeOfFinishing) {
        this.timeOfFinishing = timeOfFinishing;
    }

    public String getUrlNews() {
        return urlNews;
    }

    public void setUrlNews(String urlNews) {
        this.urlNews = urlNews;
    }
}
/*
Общая схема:
Нужна таблица с новостями (текст, срок действия с по, активность новости, урл до файла с макетом для WebView).
 */