package ru.trendtech.domain;

/**
 * Created by petr on 08.07.2015.
 */
public enum PushType {
    UNKNOWN(0),
    WAIT_PAYMENT_START(1),

    ;


    private final int value;

    PushType(int value) {
        this.value = value;
    }

    public static PushType getByValue(int value) {
        PushType result = UNKNOWN;
        for (PushType item : values()) {
            if (item.getValue() == value) {
                result = item;
                break;
            }
        }
        return result;
    }

    public int getValue() {
        return value;
    }
}
