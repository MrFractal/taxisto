package ru.trendtech.domain;

/**
 * Created by petr on 22.06.2015.
 */
public enum TabletState {
    UNKNOWN(0),
    WORK(1),
    BROKEN(2),
    REPAIR(3)
    ;

    private final int value;

    private TabletState(int value) {
        this.value = value;
    }

    public static TabletState getByValue(int value) {
        TabletState result = WORK;
        for (TabletState item : values()) {
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
