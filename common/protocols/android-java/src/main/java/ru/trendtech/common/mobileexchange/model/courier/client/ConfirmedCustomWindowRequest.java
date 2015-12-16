package ru.trendtech.common.mobileexchange.model.courier.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.courier.web.CommonRequest;

/**
 * Created by petr on 04.09.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfirmedCustomWindowRequest extends CommonRequest {
    private String typeWindow;
    /*
    values:
    REQUIRED_REQUEST, // для активации сервиса требуется запрос
    ACTIVATION_SERVICE_CONGRATULATION, // окошко с информацией об активация сервиса
    OPERATOR_IS_BUSY, // окошко с информацией о номере очереди
    REQUIRED_CARD, // требуется карта
    ALCOHOL_STOP, // после 10 бухать нельзя
    */

    public String getTypeWindow() {
        return typeWindow;
    }

    public void setTypeWindow(String typeWindow) {
        this.typeWindow = typeWindow;
    }
}
