package ru.trendtech.domain.admin;

/**
 * Created by petr on 01.09.14.
 */
public enum CurrentState {
    ONLINE(1),
    OFFLINE(0);


    private final int value;

    private CurrentState(int value) {
        this.value = value;
    }

    public static CurrentState getByValue(int value) {
        CurrentState result = OFFLINE;
        for (CurrentState item : values()) {
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
