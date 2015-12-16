package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 21.07.2015.
 */
public class ImageSourceInfo {
    private String urlImage;
    private String dimension;
    private boolean toPost;

    public boolean isToPost() {
        return toPost;
    }

    public void setToPost(boolean toPost) {
        this.toPost = toPost;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }
}
