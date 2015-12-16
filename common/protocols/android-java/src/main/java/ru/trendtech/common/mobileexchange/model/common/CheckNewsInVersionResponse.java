package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 22.01.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckNewsInVersionResponse {
    private List<NewsVersionAppInfo> newsVersionAppInfo = new ArrayList<NewsVersionAppInfo>();

    public List<NewsVersionAppInfo> getNewsVersionAppInfo() {
        return newsVersionAppInfo;
    }

    public void setNewsVersionAppInfo(List<NewsVersionAppInfo> newsVersionAppInfo) {
        this.newsVersionAppInfo = newsVersionAppInfo;
    }
}
