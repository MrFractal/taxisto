package ru.trendtech.common.mobileexchange.model.common;

/**
 * File created by max on 08/05/2014 13:23.
 */


public enum AutoClassEnum {
    STANDARD(AutoClass.STANDARD),
    COMFORT(AutoClass.COMFORT),
    BUSINESS(AutoClass.BUSINESS);

    private int value;

    AutoClassEnum(int value) {
        this.value = value;
    }

}
