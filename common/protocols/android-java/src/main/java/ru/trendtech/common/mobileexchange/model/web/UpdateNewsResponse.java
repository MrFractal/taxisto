package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.NewsInfo;

/**
 * Created by petr on 01.12.2014.
 */

public class UpdateNewsResponse extends ErrorCodeHelper{
    private NewsInfo newsInfo;

    public NewsInfo getNewsInfo() {
        return newsInfo;
    }

    public void setNewsInfo(NewsInfo newsInfo) {
        this.newsInfo = newsInfo;
    }

}
