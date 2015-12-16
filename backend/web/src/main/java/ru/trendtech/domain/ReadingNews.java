package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 01.12.2014.
 */

@Entity
@Table(name = "reading_news")
public class ReadingNews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "news_id")
    private News news;

    @Column(name = "time_reading_news")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfReading;

    @OneToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public DateTime getTimeOfReading() {
        return timeOfReading;
    }

    public void setTimeOfReading(DateTime timeOfReading) {
        this.timeOfReading = timeOfReading;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}



/*
Таблица с фактом прочтения новости (ид новости, ид таксиста, дата/время прочтения).
*/
