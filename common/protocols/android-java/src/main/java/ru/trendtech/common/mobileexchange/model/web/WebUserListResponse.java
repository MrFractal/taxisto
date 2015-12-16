package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 23.03.2015.
 */
public class WebUserListResponse extends ErrorCodeHelper{
    List<WebUserInfo> webUserInfoList = new ArrayList<WebUserInfo>();

    public List<WebUserInfo> getWebUserInfoList() {
        return webUserInfoList;
    }

    public void setWebUserInfoList(List<WebUserInfo> webUserInfoList) {
        this.webUserInfoList = webUserInfoList;
    }
}
