package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 24.09.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BindingCardResponse extends ErrorCodeHelper {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
