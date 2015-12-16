package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.CountClientHelper;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 07.11.2014.
 */
public class CountClientResponse {

    private CountClientHelper countClientHelper = new CountClientHelper();



    public CountClientHelper getCountClientHelper() {
        return countClientHelper;
    }

    public void setCountClientHelper(CountClientHelper countClientHelper) {
        this.countClientHelper = countClientHelper;
    }
}
