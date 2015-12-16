package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 12.02.2015.
 */
public class ArticleAdjustmentsRequest {
    private String security_token;
    private long articleAdjustmentsId;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getArticleAdjustmentsId() {
        return articleAdjustmentsId;
    }

    public void setArticleAdjustmentsId(long articleAdjustmentsId) {
        this.articleAdjustmentsId = articleAdjustmentsId;
    }
}
