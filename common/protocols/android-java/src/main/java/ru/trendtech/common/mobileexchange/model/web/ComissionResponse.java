package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ComissionInfo;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 17.12.2014.
 */
public class ComissionResponse extends ErrorCodeHelper{
    private List<ComissionInfo> comissionInfoList = new ArrayList<ComissionInfo>();

    public List<ComissionInfo> getComissionInfoList() {
        return comissionInfoList;
    }

    public void setComissionInfoList(List<ComissionInfo> comissionInfoList) {
        this.comissionInfoList = comissionInfoList;
    }
}
