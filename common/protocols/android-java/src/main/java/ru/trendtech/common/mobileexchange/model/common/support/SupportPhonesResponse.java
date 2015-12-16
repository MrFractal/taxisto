package ru.trendtech.common.mobileexchange.model.common.support;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 15.06.2015.
 */
public class SupportPhonesResponse extends ErrorCodeHelper {
    private SupportPhones supportPhones = new SupportPhones();

    public SupportPhones getSupportPhones() {
        return supportPhones;
    }

    public void setSupportPhones(SupportPhones supportPhones) {
        this.supportPhones = supportPhones;
    }
}
