package ru.trendtech.common.mobileexchange.model.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 06.02.2015.
 */
public class TipPercentResponse extends ErrorCodeHelper{
    private List<TipPercentInfo> tipPercentInfos = new ArrayList<TipPercentInfo>();

    public List<TipPercentInfo> getTipPercentInfos() {
        return tipPercentInfos;
    }

    public void setTipPercentInfos(List<TipPercentInfo> tipPercentInfos) {
        this.tipPercentInfos = tipPercentInfos;
    }
}
