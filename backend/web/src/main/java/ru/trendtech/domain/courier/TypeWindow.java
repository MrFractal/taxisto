package ru.trendtech.domain.courier;

/**
 * Created by petr on 03.09.2015.
 */
public enum TypeWindow {
    REQUIRED_REQUEST, // для активации сервиса требуется запрос
    ORDER_CONFIRMED, // заказ подтвержден
    ACTIVATION_SERVICE_CONGRATULATION, // окошко с информацией об активация сервиса
    OPERATOR_IS_BUSY, // окошко с информацией о номере очереди
    REQUIRED_CARD, // требуется карта
    ALCOHOL_STOP, // после 10 бухать нельзя
    FINE_CANCELING, // штраф за отмену заказа
    ORDER_CANCELING, // заказ отменен
    COURIER_ESTIMATED, // оценка курьера
    REQUIRED_THREED_SECURE, // требуется пройти 3dssecure авторизацию
    SERVICE_UNAVAILABLE,  // сервис недоступен
    WAIT_HOLD, // ожидание холда
    TOO_YOUNG // мой друг, ты слишком молод!
}
