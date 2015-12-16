package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.NewsInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 01.12.2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverNewsResponse {
    private ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();
    private List<NewsInfo> newsInfoList = new ArrayList<NewsInfo>();

    public ErrorCodeHelper getErrorCodeHelper() {
        return errorCodeHelper;
    }

    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
        this.errorCodeHelper = errorCodeHelper;
    }

    public List<NewsInfo> getNewsInfoList() {
        return newsInfoList;
    }

    public void setNewsInfoList(List<NewsInfo> newsInfoList) {
        this.newsInfoList = newsInfoList;
    }
}
