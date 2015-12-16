package ru.trendtech.common.mobileexchange.model.web;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 20.03.2015.
 */
public class DeleteNewsRequest {
    private String security_token;
    private List<Long> newsIds = new ArrayList<Long>();

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public List<Long> getNewsIds() {
        return newsIds;
    }

    public void setNewsIds(List<Long> newsIds) {
        this.newsIds = newsIds;
    }
}
