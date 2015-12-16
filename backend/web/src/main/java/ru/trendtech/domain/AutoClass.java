package ru.trendtech.domain;

public enum AutoClass {
    UNKNOWN(0),
    STANDARD(1),
    COMFORT(2),
    BUSINESS(3),
    LOW_COSTER(4),
    BONUS(5)
    ;

    private final int value;

    private AutoClass(int value) {
        this.value = value;
    }

    public static AutoClass getByValue(int value) {
        AutoClass result = STANDARD;
        for (AutoClass item : values()) {
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
