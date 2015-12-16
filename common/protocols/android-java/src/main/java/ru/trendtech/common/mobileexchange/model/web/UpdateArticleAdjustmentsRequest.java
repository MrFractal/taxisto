package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 12.02.2015.
 */
public class UpdateArticleAdjustmentsRequest {
    private String security_token;
    private ArticleAdjustmentsInfo articleAdjustmentsInfo;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public ArticleAdjustmentsInfo getArticleAdjustmentsInfo() {
        return articleAdjustmentsInfo;
    }

    public void setArticleAdjustmentsInfo(ArticleAdjustmentsInfo articleAdjustmentsInfo) {
        this.articleAdjustmentsInfo = articleAdjustmentsInfo;
    }
}
