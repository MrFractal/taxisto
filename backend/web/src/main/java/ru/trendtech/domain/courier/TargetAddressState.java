package ru.trendtech.domain.courier;

/**
 * Created by petr on 02.11.2015.
 */
public enum TargetAddressState {
    UNKNOWN(0),
    CURRENT_ADDRESS(1),
    TOOK(2),
    DELIVERED(3),
    PROBLEM(4);

    private final int value;

    TargetAddressState(int value) {
        this.value = value;
    }

    public static TargetAddressState getByValue(int value) {
        TargetAddressState result = UNKNOWN;
        for (TargetAddressState item : values()) {
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
