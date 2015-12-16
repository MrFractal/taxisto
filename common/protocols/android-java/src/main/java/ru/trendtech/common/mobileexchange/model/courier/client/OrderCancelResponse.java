package ru.trendtech.common.mobileexchange.model.courier.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.courier.CustomWindowInfo;

/**
 * Created by petr on 10.09.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderCancelResponse extends ErrorCodeHelper {
    private CustomWindowInfo customWindowInfo;

    public CustomWindowInfo getCustomWindowInfo() {
        return customWindowInfo;
    }

    public void setCustomWindowInfo(CustomWindowInfo customWindowInfo) {
        this.customWindowInfo = customWindowInfo;
    }
}
