package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.courier.web.CommonRequest;

/**
 * Created by petr on 22.09.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressByGeoPointRequest extends CommonRequest {
    private String point; // format: 82.944653,55.021355

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
