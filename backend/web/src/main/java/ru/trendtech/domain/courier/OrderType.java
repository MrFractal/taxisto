package ru.trendtech.domain.courier;

/**
 * Created by petr on 20.08.2015.
 */
public enum OrderType {
    UNKNOWN(0),
    BUY_AND_DELIVER(1),
    TAKE_AND_DELIVER(2),
    OTHER(3);

    private final int value;

    OrderType(int value) {
        this.value = value;
    }

    public static OrderType getByValue(int value) {
        OrderType result = UNKNOWN;
        for (OrderType item : values()) {
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
