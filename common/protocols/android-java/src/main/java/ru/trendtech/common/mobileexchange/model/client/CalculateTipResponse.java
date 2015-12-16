package ru.trendtech.common.mobileexchange.model.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.TipPercentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 09.02.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalculateTipResponse {
    private List<TipPercentInfo> tipPercentInfos = new ArrayList<TipPercentInfo>();
    private int sum;

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public List<TipPercentInfo> getTipPercentInfos() {
        return tipPercentInfos;
    }

    public void setTipPercentInfos(List<TipPercentInfo> tipPercentInfos) {
        this.tipPercentInfos = tipPercentInfos;
    }

}
