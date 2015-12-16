package ru.trendtech.common.mobileexchange.model.courier.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 03.11.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public enum TargetAddressStateInfo {
    UNKNOWN(0),
    CURRENT_ADDRESS(1),
    TOOK(2),
    DELIVERED(3),
    PROBLEM(4);

    private final int value;

    TargetAddressStateInfo(int value) {
        this.value = value;
    }

    public static TargetAddressStateInfo getByValue(int value) {
        TargetAddressStateInfo result = UNKNOWN;
        for (TargetAddressStateInfo item : values()) {
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
