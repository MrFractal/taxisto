package ru.trendtech.common.mobileexchange.model.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 22.01.2015.
 */
public class ListNewsInVersionResponse {
    private List<NewsVersionAppInfo> newsVersionAppInfoList = new ArrayList<NewsVersionAppInfo>();

    public List<NewsVersionAppInfo> getNewsVersionAppInfoList() {
        return newsVersionAppInfoList;
    }

    public void setNewsVersionAppInfoList(List<NewsVersionAppInfo> newsVersionAppInfoList) {
        this.newsVersionAppInfoList = newsVersionAppInfoList;
    }
}
