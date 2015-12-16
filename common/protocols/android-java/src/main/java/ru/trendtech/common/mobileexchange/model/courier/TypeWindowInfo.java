package ru.trendtech.common.mobileexchange.model.courier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 18.09.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public enum TypeWindowInfo {
    REQUIRED_REQUEST, // для активации сервиса требуется запрос
    ORDER_CONFIRMED, // заказ подтвержден
    ACTIVATION_SERVICE_CONGRATULATION, // окошко с информацией об активация сервиса
    OPERATOR_IS_BUSY, // окошко с информацией о номере очереди
    REQUIRED_CARD, // требуется карта
    ALCOHOL_STOP, // после 10 бухать нельзя
    FINE_CANCELING, // штраф за отмену заказа
    ORDER_CANCELING, // заказ отменен
    COURIER_ESTIMATED, // оценка курьера
    REQUIRED_THREED_SECURE, // требуется пройти 3dsecure авторизацию
    SERVICE_UNAVAILABLE,  // сервис недоступен
    TOO_YOUNG // мой друг, ты слишком молод
}
