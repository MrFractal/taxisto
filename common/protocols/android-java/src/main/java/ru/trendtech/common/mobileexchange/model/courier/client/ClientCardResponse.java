package ru.trendtech.common.mobileexchange.model.courier.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.courier.CourierClientCardInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 02.10.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientCardResponse extends ErrorCodeHelper {
    private List<CourierClientCardInfo> courierClientCardInfos = new ArrayList<CourierClientCardInfo>();

    public List<CourierClientCardInfo> getCourierClientCardInfos() {
        return courierClientCardInfos;
    }

    public void setCourierClientCardInfos(List<CourierClientCardInfo> courierClientCardInfos) {
        this.courierClientCardInfos = courierClientCardInfos;
    }
}
