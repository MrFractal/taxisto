package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.MdOrderInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 05.11.2014.
 */
public class MdOrderClientResponse {
    private List<MdOrderInfo> mdOrderInfoList = new ArrayList<MdOrderInfo>();
    private ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();

    public List<MdOrderInfo> getMdOrderInfoList() {
        return mdOrderInfoList;
    }

    public void setMdOrderInfoList(List<MdOrderInfo> mdOrderInfoList) {
        this.mdOrderInfoList = mdOrderInfoList;
    }

    public ErrorCodeHelper getErrorCodeHelper() {
        return errorCodeHelper;
    }

    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
        this.errorCodeHelper = errorCodeHelper;
    }
}
