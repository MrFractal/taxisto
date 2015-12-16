package ru.trendtech.domain;

import javax.persistence.*;

/**
 * Created by petr on 12.02.2015.
 */
@Entity
@Table(name = "article_adjustment")
public class ArticleAdjustments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

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
}
