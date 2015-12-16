package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.NewsInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 04.02.2015.
 */
public class NewsResponse extends ErrorCodeHelper{
   private List<NewsInfo> newsInfos = new ArrayList<NewsInfo>();

   public List<NewsInfo> getNewsInfos() {
       return newsInfos;
   }

   public void setNewsInfos(List<NewsInfo> newsInfos) {
       this.newsInfos = newsInfos;
   }
}
