package ru.trendtech.domain.courier;

/**
 * Created by petr on 24.09.2015.
 */
    public enum PaymentState {
        NONE,
        WAIT_TO_HOLD,
        REQUIRED_THREED_SECURE,
        HOLD, // сумма захолдирована
        CANCELED, // отмена холда
        FAILED_HOLD, // произошла ошибка при холдировании
        FAILED_PAYMENT, // произошла ошибка при завершении оплаты
        GOOD_PAYMENT, // удачная оплата
        REFUND, // возврат средств
    }
