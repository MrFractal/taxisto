package ru.trendtech.common.mobileexchange.model.driver;

import ru.trendtech.common.mobileexchange.model.courier.web.CommonRequest;

public class SupportPhoneRequest extends CommonRequest {
    private int city;

    public SupportPhoneRequest(int city) {
        this.city = city;
    }

    public int getCity() {
        return city;
    }
}
