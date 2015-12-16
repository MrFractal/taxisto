package ru.trendtech.common.mobileexchange.model.courier.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.DriverTypeInfo;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.SystemConfiguration_V3_Response;
import ru.trendtech.common.mobileexchange.model.courier.OrderInfo;

/**
 * Created by petr on 17.09.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourierConfigurationResponse extends ErrorCodeHelper {
    private DriverTypeInfo driverTypeInfo;
    private SystemConfiguration_V3_Response taxistoConfiguration = new SystemConfiguration_V3_Response();
    private OrderInfo orderInfo;

    public DriverTypeInfo getDriverTypeInfo() {
        return driverTypeInfo;
    }

    public void setDriverTypeInfo(DriverTypeInfo driverTypeInfo) {
        this.driverTypeInfo = driverTypeInfo;
    }

    public SystemConfiguration_V3_Response getTaxistoConfiguration() {
        return taxistoConfiguration;
    }

    public void setTaxistoConfiguration(SystemConfiguration_V3_Response taxistoConfiguration) {
        this.taxistoConfiguration = taxistoConfiguration;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
}
