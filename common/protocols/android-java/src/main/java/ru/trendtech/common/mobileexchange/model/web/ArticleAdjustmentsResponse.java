package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 12.02.2015.
 */
public class ArticleAdjustmentsResponse extends ErrorCodeHelper {
   private List<ArticleAdjustmentsInfo> articleAdjustmentsInfos = new ArrayList<ArticleAdjustmentsInfo>();

    public List<ArticleAdjustmentsInfo> getArticleAdjustmentsInfos() {
        return articleAdjustmentsInfos;
    }

    public void setArticleAdjustmentsInfos(List<ArticleAdjustmentsInfo> articleAdjustmentsInfos) {
        this.articleAdjustmentsInfos = articleAdjustmentsInfos;
    }
}
