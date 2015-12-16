package ru.trendtech.domain;

import javax.persistence.*;

/**
 * Created by petr on 06.07.2015.
 */
@Entity
@Table(name = "image_source")
public class ImageSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "dimension")
    private String dimension;

    @Column(name = "to_post" , nullable = false, columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean toPost;

    public boolean isToPost() {
        return toPost;
    }

    public void setToPost(boolean toPost) {
        this.toPost = toPost;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

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

}
