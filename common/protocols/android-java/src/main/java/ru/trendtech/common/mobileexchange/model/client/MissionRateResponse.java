package ru.trendtech.common.mobileexchange.model.client;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.rates.MissionRateInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 11.11.2014.
 */
public class MissionRateResponse {
    private List<MissionRateInfo> rates = new ArrayList<MissionRateInfo>();
    private ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();

    public List<MissionRateInfo> getRates() {
        return rates;
    }

    public void setRates(List<MissionRateInfo> rates) {
        this.rates = rates;
    }

    public ErrorCodeHelper getErrorCodeHelper() {
        return errorCodeHelper;
    }

    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
        this.errorCodeHelper = errorCodeHelper;
    }
}
