package ru.trendtech.domain;

public enum Operation {
    MISSION(0),
    FINE(1),
    INCOME(2),
    CASHING(3);

    private final int value;

    private Operation(int value) {
        this.value = value;
    }

    public static Operation getByValue(int value) {
        Operation result = MISSION;
        for (Operation item : values()) {
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
