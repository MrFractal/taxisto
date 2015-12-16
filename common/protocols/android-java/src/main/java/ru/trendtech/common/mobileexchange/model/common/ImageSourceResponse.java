package ru.trendtech.common.mobileexchange.model.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 06.07.2015.
 */
public class ImageSourceResponse extends ErrorCodeHelper {
    private List<ImageSourceInfo> infoList = new ArrayList<ImageSourceInfo>();

    public List<ImageSourceInfo> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<ImageSourceInfo> infoList) {
        this.infoList = infoList;
    }
}
