package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 22.01.2015.
 */
@Entity
@Table(name = "news_release")
public class NewsVersionApp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "active", columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean active;

    @OneToOne
    @JoinColumn(name = "version_app_id")
    private VersionsApp versionsApp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public VersionsApp getVersionsApp() {
        return versionsApp;
    }

    public void setVersionsApp(VersionsApp versionsApp) {
        this.versionsApp = versionsApp;
    }
}
