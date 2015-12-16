package ru.trendtech.common.mobileexchange.model.courier.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.courier.web.CommonRequest;

/**
 * Created by petr on 01.09.2015.
 */
/*
Метод получения истории заказов.
Возвращает Текущие и Завершенные заказы.
Текущие заказы:
в работе,
не подтвержден,
поиск курьера,
отменен.
По умолчанию возвращает Текущие заказы.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderHistoryRequest extends CommonRequest {
    private boolean current = true;

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }
}
