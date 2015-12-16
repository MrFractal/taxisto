package ru.trendtech.common.mobileexchange.model.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.courier.web.CommonRequest;

/**
 * Created by petr on 03.09.14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetTripDetailsTextRequest extends CommonRequest{

    private long driverId;


    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }
}


//curl -X 'POST' -H 'content-type:application/json' -d '{"missionId":75, "clientPhone":"+79030760303"}' http://dev.taxisto.ru:81/index.php/client/GetTripDetailsText