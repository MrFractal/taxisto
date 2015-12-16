package ru.trendtech.services.sms;

/**
 * File created by max on 08/05/2014 0:51.
 */


public enum TypeOfSMS {
    DRIVER_WAIT(1),
    DRIVER_WAIT_TOO_LONG(2),
    DRIVER_NOT_FOUND(3),
    CUSTOM_SMS(4),;
    private int value;

    TypeOfSMS(int value) {
        this.value = value;
    }

    public static TypeOfSMS getByValue(int value) {
        TypeOfSMS result = null;
        for (TypeOfSMS sms : values()) {
            if (sms.getValue() == value) {
                result = sms;
            }
        }
        return result;
    }

    public int getValue() {
        return value;
    }

}
