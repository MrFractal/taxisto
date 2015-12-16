package ru.trendtech.common.mobileexchange.model.courier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 21.08.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentInfo {
    private long id;
    private String comment;
    private long timeOfComment;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getTimeOfComment() {
        return timeOfComment;
    }

    public void setTimeOfComment(long timeOfComment) {
        this.timeOfComment = timeOfComment;
    }
}
