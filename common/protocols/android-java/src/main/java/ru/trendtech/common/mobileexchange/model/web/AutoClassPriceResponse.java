package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.rates.AutoClassRateInfoV2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 17.12.2014.
 */
public class AutoClassPriceResponse extends ErrorCodeHelper{
    private List<AutoClassRateInfoV2> autoClassRateInfoV2List = new ArrayList<AutoClassRateInfoV2>();

    public List<AutoClassRateInfoV2> getAutoClassRateInfoV2List() {
        return autoClassRateInfoV2List;
    }

    public void setAutoClassRateInfoV2List(List<AutoClassRateInfoV2> autoClassRateInfoV2List) {
        this.autoClassRateInfoV2List = autoClassRateInfoV2List;
    }
}
