package ru.trendtech.common.mobileexchange.model.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 12.05.2015.
 */
public class ReasonResponse extends ErrorCodeHelper{
    private List<ReasonInfo> reasonInfos = new ArrayList<ReasonInfo>();

    public List<ReasonInfo> getReasonInfos() {
        return reasonInfos;
    }

    public void setReasonInfos(List<ReasonInfo> reasonInfos) {
        this.reasonInfos = reasonInfos;
    }
}
