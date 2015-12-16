package ru.trendtech.domain.admin;

/**
 * Created by petr on 01.09.14.
 */
public enum AdminStatus {
    BLOCKED(1),
    NOT_BLOCKED(0);


    private final int value;

    private AdminStatus(int value) {
        this.value = value;
    }

    public static AdminStatus getByValue(int value) {
        AdminStatus result = NOT_BLOCKED;
        for (AdminStatus item : values()) {
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
