package ru.trendtech.common.mobileexchange.model.common.support;

import java.util.HashMap;

/**
 * File created by max on 20/05/2014 20:39.
 */


public class SupportPhones {
    private HashMap<Long, SupportPhone> phones = new HashMap<Long, SupportPhone>();

    public HashMap<Long, SupportPhone> getPhones() {
        return phones;
    }
}
