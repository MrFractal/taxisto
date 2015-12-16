package ru.trendtech.domain.billing;

public enum PaymentType {
    UNKNOWN(0),
    CASH(1),
    CARD(2),
    BONUSES(3),
    CORPORATE_CASH(4),
    CORPORATE_CARD(5),
    CORPORATE_BILL(6),
    ;

    private final int value;

    private PaymentType(int value) {
        this.value = value;
    }

    public static PaymentType getByValue(int value) {
        PaymentType result = UNKNOWN;
        for (PaymentType item : values()) {
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
