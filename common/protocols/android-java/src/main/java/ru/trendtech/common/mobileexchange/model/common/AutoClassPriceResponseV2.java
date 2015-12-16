package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.rates.AutoClassRateInfoV3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 19.06.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AutoClassPriceResponseV2 extends ErrorCodeHelper {
    private List<AutoClassRateInfoV3> autoClassRateInfoV3List = new ArrayList<AutoClassRateInfoV3>();

    public List<AutoClassRateInfoV3> getAutoClassRateInfoV3List() {
        return autoClassRateInfoV3List;
    }

    public void setAutoClassRateInfoV3List(List<AutoClassRateInfoV3> autoClassRateInfoV3List) {
        this.autoClassRateInfoV3List = autoClassRateInfoV3List;
    }
}
